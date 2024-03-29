# API-PERSONA
Aplicacion *Spring Boot*.

Comando para ejecutar la aplicacion: 
> mvn clean install spring-boot:run

## POST
    * POST localhost:8080/v1/personas
    * Request Body Ex: {"nombre": "Pepito","apellido": "Perez","edad":30,"sexo":"Masculino"}

## GET
    * GET localhost:8080/v1/personas


## DELETE
    * DELETE localhost:8080/v1/personas/{id}


## PUT
    * PUT localhost:8080/v1/personas/{id}
    * Request Body Ex:{"nombre": "Pepito","apellido": "Perez","edad":30,"sexo":"Masculino"}


# REDIS
## Usos:
1) *CACHE*
2) *IN-MEMORY DB* (anotando @RedisHash tus objetos del model, y agregando @EnableRedisRepositories podes crear CrudRepositories y hacer consultas como si fuera una db comun y corriente) ** la durabilidad de ACID la logra con ver mecanismos de persistencia en disco duro que provee REDIS debajo (SNAPSHOTTING y AOF) **

## Configuracion Spring:
1) Agregar dependencia starter de spring-cache-redis (contiene spring-data-redis y Lettuce: cliente redis supports synchronous, asynchronous, and reactive interfaces whereas Jedis are synchronous and a Jedis connection is not thread-safe.)
2) *Conexion a Redis Server* Crear RedisConfig.java y agregar bean LettuceConnectionFactory: fabrica/pool de conexiones al server (simil a db connections), aqui definis host y port del server.
3) *Configuracion de las caches* Crear CacheConfig.java y agregar anotacion @EnableCaching y agregar Bean CacheManager con un Map<String, RedisCacheConfiguration> para definir y crear cada cache-name (nombre de cache que va a ser usado como prefijo de cada key para poder crear subconjuntos de keys agregupadas por prefijo serian como "subcaches") con su configuracion particular por ej, cada cache una puede tener su propio TTL y serializer de key y value segun el tipo de dato que sean.
Es decir:
- "user-cache": RedisCacheConfiguration(ttl=100s, keySerializer=StringRedisSerializer, valueSerializer=StringRedisSerializer)
- "product-cache": RedisCacheConfiguration(ttl=100s, keySerializer=StringRedisSerializer, valueSerializer=GenericJackson2JsonRedisSerializer)

Las KEYS quedarian con el siguiente formado de ambas dos caches pre-fijadas (KEY-PREFIX definidos aqui arriba):
- user-cache::{KEY}
- product-cache::{KEY}

Por defecto, el cliente redis usa JdkSerializationRedisSerializer para serializar y desserializar objetos. Dentro de el paquete org.springframework.data.redis.serializer tenemos todos los serializadores disponibles cada uno con su propias caracteristicas:
Ejemplo:
- JdkSerializationRedisSerializer, which is used by default for RedisCache and RedisTemplate.
- StringRedisSerializer: Simple String to byte[]
- GenericJackson2JsonRedisSerializer (Mapea Objecto a JSON): Generico y Simple ya viene con el ObjectMapper configurado (usa este en caso de no necesitas customizar el objectmapper)
- Jackson2JsonRedisSerializer (Mapea Objecto a JSON): Similar a GenericJackson2JsonRedisSerializer pero este te permite configurar un ObjectMapper Custom y pasarselo es decir podes customizar aun mas la configuracion del ObjectMapper para que lo tenga en cuenta para la serializacion y descerializacion 

### Nota:
Usando *JdkSerializationRedisSerializer* el que viene por defecto serializa asi un objeto es decir asi veras la data guardada de un objeto en el redis-server, ejecuta un get en el redis-client para chequear:
> get personas-cache::1
> output: "\xac\xed\x00\x05sr\x00\x1dcom.api.persona.model.Persona\x00\x00\x00\x00\x00\x00\x00\x01\x02\x00\x05J\x00\x02idL\x00\bapellidot\x00\x12Ljava/lang/String;L\x00\x04edadt\x00\x13Ljava/lang/Integer;L\x00\x06nombreq\x00~\x00\x01L\x00\x04sexoq\x00~\x00\x01xp\x00\x00\x00\x00\x00\x00\x00\x01t\x00\x05Perezsr\x00\x11java.lang.Integer\x12\xe2\xa0\xa4\xf7\x81\x878\x02\x00\x01I\x00\x05valuexr\x00\x10java.lang.Number\x86\xac\x95\x1d\x0b\x94\xe0\x8b\x02\x00\x00xp\x00\x00\x00\x1dt\x00\x04Rault\x00\tMasculino"

Usando *GenericJackson2JsonRedisSerializer*
> get personas-cache::1
> output: "{\"@class\":\"com.api.persona.model.Persona\",\"id\":1,\"nombre\":\"Raul\",\"apellido\":\"Perez\",\"edad\":29,\"sexo\":\"Masculino\"}"

### Resumen:
- *Jedis*:   sync y no thread safe connections
- *Lettuce*: sync + async + reactive

# @Cacheable
## Conditional caching:
- unless = "..." Se evalua DESPUES de ejecutarse el metodo y se debe cumplir para que NO se almacene la entry en cache. (el caso de si el objeto retornado es null ya esta configurado asi que no es necesario hacer unless = #result == null)
- Ejemplo:
- unless="#result == null" (indica que no queres que se almacene una entry nueva en caso de que el objeto retornado del metodo representado por result sea null, esto podes aplicarlo a nivel de configuracion RedisCacheConfiguration.disableCachingNullValues() para no tenes que poner el unless en todas las anotaciones @Cacheable)
- condition = "..." : Se evalua ANTES de ejecutarse el metodo y se debe cumplir para que se almacene la entry en cache.
- Ejemplos: 
- condition="#name.length < 32" (indica que queres que se almacene la entry si es mejor a 32 el largo del nombre)
- condition="#isCacheable" Podes pasar un argumento a tu metodo asi le pasas un flag boolean para indicarle si queres cachearlo o no desde la invocacion es decir "Persona metodo(Long id, boolean isCacheable)"

### Mecanismos de persistencia/almacenamiento de data en disco duro
- *SNAPSHOTTING (RDB)* por defecto activo (configurable en redis.conf) Cada X tiempo guarda en disco, Ventaja: mas rapido, Desventaja: Se cae el sv y podes perder data insertada entre el tiempo sandwich de guardado y el momento final que se cayo
- *APPEND ONLY FILE (AOF)* por defecto inactivo (configurable en redis.conf) Cada escritura es guardada en disco como un evento, V: la data no se pierde, D: mas lento

## Comandos utiles REDIS
- KEYS * muestra las keys (cache-name::key)
Ejemplo: 
1) "personas-cache::2"
2) "personas-cache::1"
3) "personas-cache::0"

- HGETALL cacheName::key: Para obtener valores de entry con tipo de dato hash (Ej: HGETALL "persona-cache::1")
- GET cacheName::key: Para obtener valores de entry con tipo de dato string (Ej: GET "persona-cache::1")
Ejemplo asi guarda una entry de objeto de tipo persona con GenericJackson2JsonRedisSerializer:
> "{\"@class\":\"com.api.persona.model.Persona\",\"id\":1,\"nombre\":\"Raul\",\"apellido\":\"Perez\",\"edad\":29,\"sexo\":\"Masculino\"}"
- FLUSHALL hace un reset del server borrando la data guardada previamente
- TYPE *key-name* para saber el tipo de dato para desp poder saber que get usar para obtener el valor

## Cliente GUI (Interfaz grafica)
- RedisInsight (muy completa, se actualiza en tiempo real)


