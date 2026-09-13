# Actividad 4 - Calidad de software

## Integrante

- Nombre: Jairo Antonio Naranjo Ramírez

## Repositorio

- Repositorio: https://github.com/Genrex-code/unit_testing_code_examples_wrapper
- Rama: `actividad-calidad`
- Archivo directo: https://github.com/Genrex-code/unit_testing_code_examples_wrapper/blob/actividad-calidad/ACTIVIDAD_CALIDAD.md
- Último commit: se indica en la entrega de Canvas después de realizar el commit final.

## Parte 2 - Unit testing

### Estado inicial

Antes de modificar el proyecto se ejecutó `sh mvnw clean test`. El resultado fue `BUILD SUCCESS`, con 9 pruebas ejecutadas: 5 de `ParkingFeeCalculator` y 4 de `UsernamePolicy`. No se presentaron fallos, errores ni pruebas omitidas.

### Pruebas agregadas

Se conservaron las cinco pruebas originales de `ParkingFeeCalculatorTest.java` y se agregaron nueve pruebas nuevas. Se utilizaron nombres descriptivos y, cuando era razonable, la estructura Arrange, Act y Assert.

| Prueba agregada | Entrada | Resultado esperado | Comportamiento comprobado |
|---|---|---:|---|
| `zeroMinutesShouldBeFree` | 0 minutos, boleto normal | $0 | Menor valor válido |
| `sixteenMinutesShouldChargeBaseFee` | 16 minutos, boleto normal | $20 | Primera frontera después del periodo gratuito |
| `thirtyMinutesShouldChargeBaseFee` | 30 minutos, boleto normal | $20 | Caso normal dentro de la tarifa base |
| `sixtyMinutesShouldStillChargeBaseFee` | 60 minutos, boleto normal | $20 | Límite superior de la primera hora |
| `oneHundredTwentyMinutesShouldChargeExactlyOneAdditionalHour` | 120 minutos, boleto normal | $35 | Hora adicional completa |
| `oneHundredTwentyOneMinutesShouldChargeTwoStartedAdditionalHours` | 121 minutos, boleto normal | $50 | Inicio de una segunda hora adicional |
| `twoHundredFortyMinutesShouldRemainBelowMaximumFee` | 240 minutos, boleto normal | $65 | Valor inmediato anterior al cobro máximo |
| `twoHundredFortyOneMinutesShouldReachMaximumFee` | 241 minutos, boleto normal | $80 | Inicio del límite máximo |
| `lostTicketShouldCostOneHundredFiftyEvenForLongParkingTime` | 600 minutos, boleto perdido | $150 | Condición especial combinada con una estancia extensa |

Después de agregar las pruebas se obtuvieron 18 pruebas exitosas: 14 de estacionamiento y 4 de usuarios.

### P1. ¿Por qué probar muchos valores de una misma región no necesariamente mejora mucho una suite de pruebas?

Porque varios valores dentro de una misma región recorren exactamente la misma regla y producen el mismo comportamiento. Por ejemplo, 30 y 40 minutos pertenecen al intervalo de 16 a 60 minutos y ambos deben costar $20. Probar demasiados valores equivalentes aumenta el tiempo y el mantenimiento de la suite, pero aporta poca información nueva. Resulta más útil elegir un representante del intervalo y dedicar más pruebas a las fronteras y condiciones especiales.

### P2. Menciona dos fronteras importantes y explica por qué vale la pena probar valores cercanos.

La primera frontera está entre 15 y 16 minutos: a los 15 minutos el estacionamiento es gratuito, mientras que a los 16 comienza el cobro de $20. La segunda se encuentra entre 60 y 61 minutos: a los 60 minutos todavía se cobra la tarifa base, pero a los 61 inicia una hora adicional y el total sube a $35. Probar ambos lados permite detectar comparaciones incorrectas, como utilizar `<` en lugar de `<=`, y errores de una unidad.

### P3. ¿Que todas las pruebas estén en verde demuestra que el programa es correcto?

No. Solamente demuestra que el programa cumplió los casos incluidos en la suite bajo el entorno utilizado. Puede haber requisitos mal interpretados, caminos no probados, problemas de integración, datos inesperados o defectos que las pruebas actuales no contemplan. Las pruebas reducen la incertidumbre, pero no constituyen una demostración absoluta de corrección.

## Code review manual

La revisión se realizó antes de consultar SonarQube for IDE. Los números de línea de las pruebas corresponden a la versión original del ZIP.

| Archivo y línea | Hallazgo | Tipo | Severidad | Propuesta |
|---|---|---|---|---|
| `ParkingFeeCalculator.java:6-11` | La condición de boleto perdido devuelve $150 antes de validar los minutos. La combinación de boleto perdido y tiempo negativo deja ambigua la prioridad entre ambas reglas. | Design | Media | Confirmar la prioridad con el responsable del negocio y documentarla; después agregar una prueba específica. |
| `ParkingFeeCalculator.java:7-25` | Tarifas, umbrales y máximo se expresan mediante números literales; algunos valores se repiten. | Maintainability | Baja | Extraer constantes con nombres relacionados con las reglas del estacionamiento. |
| `ParkingFeeCalculatorTest.java:10-29` | Las pruebas originales no cubrían los valores 0, 16, 60 ni el cambio entre 120 y 121 minutos. | Testing | Media | Agregar pruebas a ambos lados de las transiciones y para el mínimo válido. |
| `ParkingFeeCalculatorTest.java:31-37` | El límite máximo se comprobaba únicamente con 600 minutos y no en el punto donde comienza. | Testing | Media | Comprobar que 240 minutos cuestan $65 y que 241 alcanzan los $80. |
| `ParkingFeeCalculatorTest.java:40-56` | El boleto perdido y otros datos de entrada se probaban por separado. | Testing | Media | Combinar boleto perdido con una estancia extensa para comprobar la prioridad de la condición especial. |
| `LegacyParkingReceipt.java:14` | `plate == ""` compara referencias en vez del contenido. Una cadena vacía creada como un objeto distinto podría superar la validación. | Bug | Media | Después de validar `null`, utilizar `plate.isEmpty()`. |
| `LegacyParkingReceipt.java:26` | `buildReceipt` genera el recibo y también escribe directamente en la salida estándar. | Design | Baja | Separar el registro del formateo o utilizar el mecanismo de logging definido por el proyecto. |
| `LegacyParkingReceipt.java:28-30` | El ternario que devuelve `true` o `false` y la comparación `free == true` son redundantes. | Readability | Baja | Utilizar `boolean free = fee == 0` e `if (free)`. |
| `LegacyParkingReceipt.java:7` | La variable `result` se inicializa con una cadena vacía que nunca se utiliza, porque ambas ramas asignan otro valor. | Maintainability | Baja | Declararla sin valor inicial o retornar el resultado directamente desde cada rama. |

## Análisis con SonarQube for IDE

El análisis se realizó localmente en Visual Studio Code antes de corregir la clase legacy. SonarQube no reportó problemas en `ParkingFeeCalculator.java` ni en `ParkingFeeCalculatorTest.java`.

| Archivo/línea | Regla o mensaje de Sonar | Explicación con mis palabras | ¿Estoy de acuerdo? |
|---|---|---|---|
| `LegacyParkingReceipt.java:14` | `java:S4973` - Strings and Boxed types should be compared using `equals()` | El operador `==` puede comparar la identidad de dos objetos `String` en lugar de verificar su contenido. | Sí. Puede permitir una placa vacía dependiendo de cómo fue construida la cadena, por lo que es un defecto funcional. |
| `LegacyParkingReceipt.java:26` | `java:S106` - Replace this use of `System.out` by a logger | La clase mezcla la creación del recibo con una salida directa que no puede administrarse por nivel o destino. | Parcialmente. En un sistema de producción conviene utilizar logging; en este ejercicio pequeño tiene una prioridad menor y no afecta el cálculo. |
| `LegacyParkingReceipt.java:28` | `java:S1125` - Remove the unnecessary boolean literals | El ternario `fee == 0 ? true : false` repite el resultado booleano que ya produce la comparación. | Sí. Puede simplificarse sin cambiar el comportamiento. |
| `LegacyParkingReceipt.java:30` | `java:S1125` - Remove the unnecessary boolean literal | Comparar `free == true` agrega texto sin proporcionar información. | Sí. `if (free)` expresa la misma condición con mayor claridad. |

## Code review vs. SonarQube

### P4. ¿Qué problema encontró Sonar que no habías identificado durante el code review manual?

En este ejemplo Sonar no encontró una categoría completamente nueva, porque la revisión manual ya había identificado la comparación de cadenas, la salida por consola y la lógica booleana redundante. Sin embargo, Sonar individualizó las dos ubicaciones de `java:S1125`, mientras que durante la revisión manual se habían agrupado como una sola observación. También proporcionó identificadores de regla verificables.

### P5. ¿Qué observación hiciste tú que Sonar no reportó?

La revisión manual identificó que la inicialización de `result` no se aprovecha y que existe una ambigüedad entre validar minutos negativos y aplicar primero la tarifa por boleto perdido. Sonar tampoco señaló las pruebas de frontera que faltaban originalmente ni puede decidir qué regla de negocio debe tener prioridad.

### P6. ¿Todos los hallazgos de Sonar tienen la misma importancia?

No. La comparación de cadenas mediante `==` puede causar que una entrada inválida sea aceptada y, por ello, representa un posible defecto funcional. En cambio, eliminar literales booleanos mejora principalmente la legibilidad. La recomendación de sustituir `System.out` por un logger es razonable para producción, pero resulta menos urgente en una clase pequeña utilizada con fines académicos. La prioridad depende del impacto y del contexto.

### P7. ¿Puede Sonar determinar si "$20 de 16 a 60 minutos" es la regla correcta del negocio?

No. Sonar puede analizar la estructura del código y detectar patrones conocidos, pero no conoce los acuerdos comerciales de ParkSmart. Para decidir si la tarifa es correcta necesita un requisito proporcionado por el cliente o responsable del producto y pruebas que representen ese requisito.

### P8. ¿Por qué el análisis estático no sustituye las pruebas unitarias ni el code review humano?

**a) Pruebas unitarias:** el análisis estático no ejecuta los casos de negocio ni comprueba resultados concretos. Las pruebas permiten verificar, por ejemplo, que 16 minutos cuestan $20 y que 241 minutos alcanzan el máximo de $80. También detectan regresiones cuando cambia la implementación.

**b) Code review humano:** Sonar no comprende completamente el propósito del sistema, las prioridades del negocio ni las consecuencias arquitectónicas de cada decisión. Una persona puede detectar requisitos ambiguos, pruebas faltantes, responsabilidades mezcladas y valorar si una recomendación automática resulta conveniente en el contexto real.

## Correcciones realizadas

### Corrección 1 - Comparación de la placa

- **Problema:** se utilizaba `plate == ""`, que compara referencias y podía no reconocer una cadena vacía creada como un objeto diferente.
- **Cambio:** después de conservar la validación previa de `null`, se sustituyó la condición por `plate.isEmpty()`.
- **Resultado:** la validación comprueba el contenido de manera consistente.
- **¿Sonar dejó de reportarlo?:** Sí. Después de guardar y volver a analizar el archivo desapareció `java:S4973`.

### Corrección 2 - Expresiones booleanas

- **Problema:** se utilizaban el ternario `fee == 0 ? true : false` y la condición `free == true`.
- **Cambio:** se reemplazaron por `boolean free = fee == 0` e `if (free)`.
- **Resultado:** la lógica conserva su comportamiento y resulta más directa.
- **¿Sonar dejó de reportarlo?:** Sí. Desaparecieron las ubicaciones asociadas con `java:S1125`.

El hallazgo `java:S106` relacionado con `System.out.println` se conservó conscientemente. Aunque un logger sería preferible en producción, se consideró de menor prioridad en este ejercicio y no fue necesario corregir todos los hallazgos.

## Resultado final

- Comando ejecutado: `sh mvnw clean test`
- Pruebas ejecutadas: 18
- Fallos: 0
- Errores: 0
- Omitidas: 0
- Resultado: **BUILD SUCCESS**
- Rama: `actividad-calidad`

La ejecución se realizó con el JDK 25 incluido en Android Studio y compilación dirigida a Java 17. Maven mostró una advertencia que recomienda utilizar `--release 17`, pero no se presentaron errores de compilación ni fallos en las pruebas. Después de las correcciones, SonarQube dejó únicamente el hallazgo de menor prioridad `java:S106`.
