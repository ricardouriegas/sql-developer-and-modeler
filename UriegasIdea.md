# Uriegas Idea

## Description

Tenemos X cantidad de tablas, por lo que deberiamos usar Hashmaps para establecer la relacion que existe entre estas tablas, basicamente lo que quiero hacer son 2 Objetos principalemente:

1. **Tabla**, con los siguientes atributos:

   - Nombre
   - Atributos (Hashmap)
   - Clave Primaria
   - Claves Foraneas

2. **Relacion**, con los siguientes atributos:
   - Tabla Origen
   - Tabla Destino
   - Tipo de Relacion (1 a 1, 1 a N, N a 1, N a N)

La forma de manejar estos objetos seria por medio de hashmaps el cual guardaria la tabla y sus relaciones, de esta forma podriamos acceder a las relaciones de una tabla en especifico de forma rapida.

El hashmap quedaria algo asi:

```JSON
nombre_tabla_1: {
    objeto_tabla_1, // Objeto tabla
    relaciones: [ // Lista de relaciones
        {
            tabla_origen: nombre_tabla_1,
            tabla_destino: nombre_tabla_2,
            tipo_relacion: 1 a 1
        },
        {
            tabla_origen: nombre_tabla_1,
            tabla_destino: nombre_tabla_3,
            tipo_relacion: 1 a N
        }
    
    ]
}
```

 **Nota**: _Se tendria que hacer una validacion, cuando se realize la creacion de una tabla, para que no exista una tabla con el mismo nombre. (ya que el nombre seria basicamente el id de la tabla)_

[UML Representativo de Uriegas](representacionUriegas.uxf)
