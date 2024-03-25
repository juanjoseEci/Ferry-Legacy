# Ferry-Legacy

_Ferry legacy es un proyecto para refinar nuestras habilidades en la detección de smell codes con una deuda técnica grave. Este proyecto posee varios aspectos a revisar como documentación , segmentación , pruebas unitarias entre otras que iremos revisando a lo largo del curso._

## Comenzando 🚀
_En entregas anteriores analizamos la deuda técnica del proyecto de manera general y establecimos unas posibles soluciones para reducir la misma. Ya habiendo identificado la deuda tecnica a manera general vamos a hacerlo de manera especifica , para esto usaremos herramientas de control de calidad que analicen el codigo, como lo es el caso de sonarcloud._

_Este proyecto al ser creado con una versión 0.52 no se puede analizar con la plataforma sonarcloud con GitActions, asi que optaremos por otra alternativa: Sonarlint_ 

_SonarLint es un plugin que nos permite en tiempo real ver smell codes o issues directamente en nuestro IDE en la clase que estemos parados actualmente permitiendo corregir errores encontrados de manera facil o tambien podemos realizar un escaneo detallado del proyecto para ver el estado actual del mismo_
![sonarlin0](java/src/main/resources/sonarlin0.png)

## SonarLin en acción ⚙️

_Para ver a detalle el estado del proyecto, realizaremos un escaneo total con el plugin SonarLint_
![sonarlin1](java/src/main/resources/sonarlin1.png)

_una vez finalizado el escaneo obtenemos un reporte indicando todas las issues o codesmells encontrados, revisemos el reporte:_
![sonarlin2](java/src/main/resources/sonarlin2.png)

_Abramos la clase que más issues tiene para observar como se estructuran los reportes:_
![sonarlin3](java/src/main/resources/sonarlin3.png)

_Como podemos ver en la imagen al desplegar la clase deseada vemos las issues que se encontraron ordenadas de mayor a menor impacto en un atributo de calidad especifico, identificadas con color rojo , amarillo y azul, de mayor a menor impacto respectivamente._

_Tambien se observa el atributo de clean code que se está fallando y una breve descripción , estos se ven sombreados al lado del atributo de calidad_

_Sonarlin nos da una explicación del issue y también el como arreglarlo_
![sonarlin4](java/src/main/resources/sonarlin4.png)

_Miremos otra issue con un atributo de calidad diferente :_
![sonarlin5](java/src/main/resources/sonarlin5.png)

_Podemos observar que el atributo de calidad: Reliability , en este caso el impacto es medio y nos están diciendo que la manera en que estamos comparando es incorrecta debido a que no son valores si no espacios en memoria y debemos usar la función squals_

_En el recuadro de arriba nos muestra un ejemplo de como no está bien el código y abajo cómo si se cumpliría con el atributo de calidad, adicionalmente nos proporciona varios links de referencia de CWE para el issue especifico_
![sonarlin6](java/src/main/resources/sonarlin6.png)

## Conclusiones 🩹
_El estado actual del proyecto es grave ya que poseemos 83 issues a resolver que sin duda son posibles con lo que queda del curso_
_El proyecto incumple varios atributos de calidad y de clean code, teniendo una deuda técnica considerable_
_El Plugin SonarLin nos es de gran ayuda debido a la versión del proyecto que no permite ser analizado de manera local ni por un workflow en gitActions con SonarCloud , siendo asi  una alternativa que nos permite analizar el código de una manera facil y rapida._
## Construido con 🛠️

_Menciona las herramientas que utilizaste para crear tu proyecto_

* [github](https://github.com/) - Gestión de Versiones
* [gradle](https://gradle.org/) - Manejador de dependencias
* [Sonarlint](https://www.sonarsource.com/products/sonarlint/) - Análisis de código

## Autores ✒️

* **Juan José Álvarez Beltrán** - *Trabajo Inicial* - [JuanAlvarezEci](https://github.com/juanalvarezeci)


## Licencia 📄

Este proyecto está bajo la Licencia MIT  - mira el archivo [LICENSE.md](LICENSE.md) para detalles
