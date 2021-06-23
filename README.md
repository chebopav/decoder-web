# decoder-web
Условия для паттерна: каждый буквенный символ обозначается "A", числовой "N", пробел "\_", в скобках пишется необязательные символы, также ставится * в случае неопределенного возможного количества символов. Блоки начинаются с "B-". Также есть обозначения U, что значит уникальное (т.е. не повторяющееся) и R - обязательное. Конец блоков - "//".</br>
Пример паттерна:</br>
B-FirstBlock-UR   (Начало основного блока с названием FirstBlock)</br>
FirstLineOfFirstBlock AAA_NNN(AN)-U    (Первая строчка основного блока с названием FirstLineOfFirstBlock)</br>
B-InlineBlock-R    (Начало вложенного блока с названием InlineBlock)</br>
InlineString NNNNA*   (Строчка вложенного блока с названием InlineString)</br>
//  (Конец вложенного блока)</br>
SecondLineOfFirstBlock AN.AAA(/NN) (Вторая строчка основного блока с названием SecondLineOfFirstBlock)</br>
// (Конец основного блока)</br>
</br>
</br>
Пример запроса: </br>
curl --request POST \</br>
  --url http://localhost:8080/input \</br>
  --header 'Content-Type: application/json' \</br>
  --data '{</br>
	"pattern" : "B-SSM-UR\nTimeZone AAA-U\ndateAndCode NNAAANNNN(NANNN)-UR\nB-RPL-R\nflight A(A)NNN(NN)-UR\nperiod NNAAANN_NNAAANN_N-UR\nBoardDescription A(A\_)NNN(N)\_.(CNNN)(YNNN)-UR\nwaybill AAANNNN(/NN)\_AAANNNN(/NN)-R\nwayDescription AAAAAA_N(N/N)-R\n//\nsign A*_*A*_*N*-UR\n//",</br>
	"inputText" : "SSM\nUTC\n20APR26444E001\nRPL\nY7919\n01JUN21 28SEP21 2\nJ 738 .C6Y174\nNSK0845 UFA1210\nUFA1340 AAQ1650\nNSKUFA 98/1\nUFAAAQ 99/1\n//\nRPL\nY7920\n01JUN21 28SEP21 2\nJ 738 .C6Y174\nAAQ1930 UFA2215\nUFA2345 NSK0310/1\nAAQUFA 98/1\nUFANSK 99/1\n//\nSI AUTOFEED 326444"</br>
}'</br>

Который выдаст результат:</br>
{</br>
  &nbsp;&nbsp;&nbsp;&nbsp;"SSM": {</br>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"TIMEZONE": "UTC",</br>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"DATEANDCODE": "20APR26444E001",</br>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"RPL": {</br>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"FLIGHT": "Y7919",</br>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"PERIOD": "01JUN21 28SEP21 2",</br>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"BOARDDESCRIPTION": "J 738 .C6Y174",</br>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"WAYBILL": "NSK0845 UFA1210",</br>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"WAYBILL2": "UFA1340 AAQ1650",</br>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"WAYDESCRIPTION": "NSKUFA 98/1",</br>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"WAYDESCRIPTION2": "UFAAAQ 99/1"</br>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</br>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"RPL2": {</br>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"FLIGHT": "Y7920",</br>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"PERIOD": "01JUN21 28SEP21 2",</br>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"BOARDDESCRIPTION": "J 738 .C6Y174",</br>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"WAYBILL": "AAQ1930 UFA2215",</br>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"WAYBILL2": "UFA2345 NSK0310/1",</br>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"WAYDESCRIPTION": "AAQUFA 98/1",</br>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"WAYDESCRIPTION2": "UFANSK 99/1"</br>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</br>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"SIGN": "SI AUTOFEED 326444"</br>
  &nbsp;&nbsp;&nbsp;&nbsp;}</br>
}</br>
