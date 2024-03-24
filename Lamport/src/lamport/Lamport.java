/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lamport;

/**
 *
 * @author lev_alunos
 */
public class Lamport {
    
    private PortRef release;//PORTREF release: signaltype;
    private EntryRef request;//ENTRYREF request: signaltype, signaltype;
 private TYPE clockT;// = 0..MAXINT;
private int sitenumT;// = 0..n-1;
String messT;// = (req, ack, rel);
String msgT = "RECORD";
//t: messT; c: clockT; j: sitenumT END;  
    
private int MyId;
//PROCESS P(i: integer);

public Lamport(int i){
    MyId = i;
    release = new PortRef(1);
    request = new EntryRef(2);
}
public Lamport(int i, int n){
     MyId = i;
}

public void Process(int i){
    release.recieve(12);
    //
    //Seção Critica
    //
    request.send(12);
}

public void Process(int i, int n){
    
    Port Min;//PORT Min: msgT;
    
    
    
}
public void lamport(){
    
    
    
    
}

PROCESS S(n, i: integer); {S[i] de n processos S}

release: signaltype;
ENTRY request: signaltype, signaltype;
PORTREF Mout[0..n-1]: msgT;
VAR state, m: msgT;
q: ARRAY [0..n-1] OF msgT;
granted: Boolean;
PROCEDURE initialise_q;
- inicializa q sujeito a Vj: 0<=j<n | q[j]= (rel, 0, j)
PROCEDURE min(i: integer): Boolean;
- verdade se o rótulo de tempo de q[i] é menor do que os outros q[j]
i.e. Vj: 0<=j<n & i<>j ((q[i].c < q[j].c) ou (q[i].c = q[j].c e i<j))
PROCEDURE update (local, ext: clockT): clockT;
- acha o próximo valor do relógio lógico.
i.e. max(local,ext)+1 
    
        
        
        
state.c= 0;// {inicializa o relógio local}
state.j= i;// {seta o local da mensagem para i}
initialise_q;// {inicializa tabela de estados}
granted= false;// {exclusão não obtida}
while (true){
    
    
    
}
SELECT {mensagem a receber}
Min.IN(m)
=> state.c:= update(state.c, m.c); { atualiza o relógio do processo i}
CASE m.t OF
req: q[m.j]:= m; {registra o pedido}
state.t:= ack; {prepara a resposta}
Mout[m.j].SEND(state); {envia resposta}
rel: q[m.j]:= m;
ack: IF q[m.j].t <> req THEN q[m.j] := m;
END;
OR WHEN q[i].t <> req {pedido de exclusão}
request.ACCEPT(signal)
=> state.c:= state.c + 1; {atualiza relógio local}
state.t:= req;
for k:=0,n-1 do Mout[k].SEND(state);
q[i]:= state;   
    
 OR WHEN q[i].t=req {liberação de exclusão}
release.IN(signal)
=> state.c:= state.c +1; {atualiza relógio local}
state.t:= rel;
for k:=0,n-1 do Mout[k].SEND(state);
q[i]:= state;
granted:= false;
END;
IF q[i].t=req and min(i) and ~granted THEN
BEGIN {obtenção de exclusão}
request.REPLY(signal);
granted:= true;
END;
END
END.   
    
    
    
    
    
    
    
    
    
    
    
    
}
