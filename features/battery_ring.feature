Funcionalidade: Tocar alarme quando a bateria estiver carregada

Para que o alarme toque quando a bateria estiver carregada
Precisa-se saber se está carregando(carregador plugado) e se sua percentagem é de 100%
Além do soar estar ativado

Cenário de Fundo:
Dado um smartphone com android "5.0"
E seu estado de bateria:
|plugado|porcentagem|estado   |soar|
|true   |100        |carregado|true|

Regra: O alarme deve ser soado se a bateria estiver em 100% e ainda carregando

    Exemplo: Alarme não tocará, pois o dispositivo não está em 100%
    Quando verificar que o smartphone está plugado, mas ainda não em 100%
    |plugado|porcentagem|estado    |soar|
    |true   |95         |carregando|true|
    Então o alarme não irá disparar

    Exemplo: Alarme não tocará, pois o dispositivo não esta plugado
    Quando verificar que o smartphone não está plugado, mesmo estando em 100%
    |plugado|percentagem|estado       |soar|
    |false  |100        |descarregando|true|
    Então o alarme não irá disparar

    Exemplo: Alarme tocará
    Quando verificar que o smartphone está plugado e em 100%
    |plugado|percentagem|estado   |soar|
    |true   |100        |carregado|true|
    Então o alarme irá disparar
