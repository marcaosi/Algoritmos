m(joao).
m(pedro).
m(luiz).
m(tunico).
m(denis).
m(alex).
m(washington).

f(maria).
f(ana).
f(carol).
f(sumira).

ad(joao, pedro).
ad(joao, ana).
ad(pedro, carol).
ad(pedro, denis).
ad(ana, alex).
ad(maria, luiz).
ad(maria, tunico).
ad(luiz, sumira).
ad(sumira, washington).

mae(X, Y):-f(X), ad(X, Y).
pai(X, Y):-m(X), ad(X, Y).

filho(X, Y):-ad(Y, X).

avom(X, Y):-m(X), ad(X, Z), ad(Z, Y).
avof(X, Y):-f(X), ad(X, Z), ad(Z, Y).

neto(X, Y):-ad(Y, Z), ad(Z, X).

irmao(X, Y):-ad(Z, X), ad(Z, Y), not(X=Y).

tio(X, Y):-irmao(Z, X), ad(Z, Y).

primo(X, Y):-ad(Z, X), ad(W, Y), irmao(Z, W).

sobrinho(X, Y):-ad(Z, X), irmao(Z, Y).