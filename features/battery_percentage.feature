Funcionalidade: Mostrar porcentagem da bateria

Para que o usuário consiga ver o quanto sua bateria está carregada
É necessário que a porcentagem da bateria seja exibida
Além de mostrar se está descarregando, carregando ou carregado

Cenário de Fundo:
Dado um smartphone com android "5.0"
E seu estado de bateria:
|plugado|porcentagem|estado   |soar|
|true   |100        |carregado|true|

Regra: A porcentagem da bateria sempre será exibida ao usuário, independente de seus atributos

    Exemplo: A porcentagem será mostrada
    Quando verificar a porcentagem
    |plugado|porcentagem|estado       |soar|
    |falso  |50         |descarregando|true|
    Então a porcentagem será exibida
    E o alarme não soará

    Exemplo: A porcentagem será mostrada
    Quando verificar o estado
    |plugado|porcentagem|estado    |soar|
    |true   |51         |carregando|true|
    Então a porcentagem será exibida
    E o alarme não soará

    Exemplo: A porcentagem será mostrada
    Quando verificar se o dispositivo está plugado
    |plugado|porcentagem|estado    |soar|
    |true   |100        |carregado |true|
    Então a porcentagem será exibida
    E o alarme soará

    Exemplo: A porcentagem será mostrada
    Quando verificar se o dispositivo está plugado
    |plugado|porcentagem|estado    |soar|
    |false  |100        |carregado |true|
    Então a porcentagem será exibida
    E o alarme não soará

    Exemplo: A porcentagem será mostrada
    Quando verificar se o dispositivo está plugado
    |plugado|porcentagem|estado    |soar |
    |true   |100        |carregado |flase|
    Então a porcentagem será exibida
    E o alarme não soará

    Exemplo: A porcentagem será mostrada
    Quando verificar a porcentagem, o estado e se está plugado
    |plugado|porcentagem|estado       |soar|
    |false  |10         |descarregando|true|
    Então a porcentagem será exibida
    E o alarme não soará
