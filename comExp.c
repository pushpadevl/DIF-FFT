#include<string.h>
#include<stdio.h>
#include<stdlib.h>

typedef struct {
    int BASE;
    int *COEFF;
}ComplexExpression;

void mul(ComplexExpression A, ComplexExpression B){
    int N = A.BASE;
    ComplexExpression C;
    C.BASE = N;
    C.COEFF = (int *)calloc(N, sizeof(int));
    for(int i=0;i<N;i++){
        for(int j=0;j<N;j++){
            C.COEFF[(i+j)%N] += A.COEFF[i] * B.COEFF[j]; //overflow possible
        }
    }
    if((N%2)==0){
        int B2 = N/2;
        for(int i=0;i<B2;i++){
            C.COEFF[i] -= C.COEFF[i+B2];
            C.COEFF[i+B2]=0;
        }
    }
    memcpy(A.COEFF,C.COEFF,N*sizeof(int));
    free(C.COEFF);
    
}
void add(ComplexExpression A, ComplexExpression B){
    int N = A.BASE;
    //ComplexExpression C;
    //C.BASE=N;
    //C.COEFF = (int *)calloc(N, sizeof(int));
    for(int i=0;i<N;i++){
        A.COEFF[i] += B.COEFF[i];
    }
    //return C;
}
void sub(ComplexExpression A, ComplexExpression B){
    int N = A.BASE;
    //ComplexExpression C;
    //C.BASE=N;
    //C.COEFF = (int *)calloc(N, sizeof(int));
    for(int i=0;i<N;i++){
        A.COEFF[i] -= B.COEFF[i];
    }
}
/*
int main(int argc, char const *argv[])
{

    ComplexExpression A;
    ComplexExpression B;
    A.BASE=4;
    A.COEFF = (int*)calloc(A.BASE,sizeof(int));
    B.BASE=4;
    B.COEFF = (int*)calloc(A.BASE,sizeof(int));
    
    A.COEFF[0] = 1; A.COEFF[1] = 2; A.COEFF[2] = 3; A.COEFF[3] = 4;
    B.COEFF[0] = 4; B.COEFF[1] = 5; B.COEFF[2] = 9; B.COEFF[3] = 6;
    
    
    sub(A,B);
    for(int i=0;i<A.BASE;i++){
        printf("%d ",A.COEFF[i]);
    }
    return 0;
}
*/
