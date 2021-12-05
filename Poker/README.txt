1. ZASADY GRY

a) Gra przeznaczona jest od 2 do 4 graczy. 
b) Po podłączeniu się wszystkich graczy do serwera pierwszy gracz ustala opłatę wejściową (ante), która zostanie pobrana od każdego gracza na początku pierwszej rundy.
c) Każdy gracz ustala swoją ilość pieniędzy, którymi zamierza później licytować.
d) Następuje faza akcji. Każdy gracz ma do dyspozycji następujące akcje:

   1. check - gracz nie wpłaca, żadnych pieniędzy do puli,
   2. raise - gracz wpłaca do puli wartość wyższą niż aktualna stawka,
   3. fold - gracz pasuje i nie może dalej grać,

przy czym gdy pierwszy gracz zdecyduje się podbić lub spasować to następni nie mogą już sprawdzić (chceck). Jeżeli któryś z graczy zdecyduje się podbić stawkę pozostali gracze muszą wykonać jedną z poniższych czynności:

   1. call - gracz wpłaca do puli równowartość obecnej stawki,
   2. raise - gracz przebija poprzednią stawkę i zmusza pozostałych graczy do wyrównania, przebicia lub spasowania,
   3. fold - gracz pasuje i nie może dalej grać.

Gdy każdy z graczy wyrówna stawkę lub żaden z nich jej nie podniósł następuje 2 rudna gry.
e) W tej rundzie każdy gracz który spasował może wymienić dowolną ilość kart w ręce i ponownie licytować. Zasady licytacji jak w rundzie pierwszej.
f) Po zakończeniu rundy drugiej wyłaniany jest zwycięzca i wyświetlana jest wartość puli.
g) Gdy wszyscy gracze wyrażą chęć dalszej gry, rozpoczyna się nowa gra.

2. SPOSÓB URUCHAMIANIA

Należy zbudować projekt poleceniem mvn verify.
Należy uruchomić serwer z parametrem reprezentującym liczbę graczy (od 2 do 4) np z linii poleceń. (Serwer znajduje się w katalogu poker-server/target).
Należy uruchomić klienta tyle razy ile podaliśmy w argumencie serwera (klient znajduje się w katalogu poker-client/target).

3. KOMUNIKATY

Serwer wyświetla informacje o rozpoczęciu i zakończeniu gry.
Każdy klient jest informowany o ruchach innego gracza po jego wykonaniu np. Player player_name checks.
Zwycięzca jest ogłaszany wszystkim graczom pod koniec rozgrywki.

