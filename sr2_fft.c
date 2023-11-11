#include "comExp.c"

int nextInSeq(int x, int L){ // L is bit length, FFT next sequence
    int y=1<<(L-1);
    x = x^y;
    while(x<y){
        y >>=1;
        x ^=y;
    }
    return x;
}
void butterfly_sr2_fft(ComplexExpression *A,int N){
    int *tmparr;
    ComplexExpression w; //omega
    ComplexExpression ww;
    ComplexExpression tmp;
    w.BASE=N;
    w.COEFF = (int *)calloc(N, sizeof(int));
    ww.BASE = N;
    ww.COEFF = (int *)calloc(N, sizeof(int)*N);
    tmp.BASE=N;
    tmp.COEFF = (int *)calloc(N, sizeof(int)*N);
        

    int L=N;
    int M=1;
    int noOfBits=0;
    int L2=L/2;
    w.COEFF[M]=1; // nth root of unity
    
    while(M!=N){

        for(int m=0;m<M;m++){
            int i=m*L;
            int k=0;
            while(k++ < L2){
                memcpy(tmp.COEFF,A[i].COEFF,sizeof(int)*N);
                add(A[i],A[i+L2]);
                sub(tmp,A[i+L2]);
                memcpy(A[i+L2].COEFF,tmp.COEFF,sizeof(int)*N);
                i++;
            }
        }

        for(int m=0;m<M;m++){
            int i=m*L + L2 + 1; 
            int k=1;    // w^0 =1 so start from 1
            memcpy(ww.COEFF,w.COEFF,sizeof(int)*N);
            while(k++ < L2){
                mul(A[i],ww);
                mul(ww,w);
                i++;
            }
        }
        w.COEFF[M]=0;
        L /=2;
        L2=L2/2;
        M *=2;
        noOfBits++;
        w.COEFF[M]=1; // now it becomes N/2 root of unity
    }
    int k=0;
    for(int i=0;i<N;i++){
        for(int j=0;j<N/2;j++){
            if(A[k].COEFF[j]!=0)
                printf("%dw^%d\t",A[k].COEFF[j],j);
        }
        printf("\n");
        k=nextInSeq(k,noOfBits);
    }
}


int main(int argc, char const *argv[]){
    int N = 8;
    ComplexExpression A[N];
    for(int i=0;i<N;i++){
        A[i].BASE = N;
        A[i].COEFF = (int *)calloc(N,sizeof(int));
        A[i].COEFF[0] = i+1;
    }
    //A[0].COEFF[0] = 1; A[1].COEFF[0] = 2; A.COEFF[2] = 3; A.COEFF[3] = 4;
    

    butterfly_sr2_fft(A,N);
    
    
    return 0;
}