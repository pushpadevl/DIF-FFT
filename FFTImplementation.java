// JAVA 8  
// w = e^(-2*pi*i/N)
import java.util.Scanner;

public class FFTImplementation {
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        System.out.print("\nEnter N: ");
        
        int N=sc.nextInt();
        int arr[] = new int[N];
        ComplexExpression A[] = new ComplexExpression[N];
        System.out.print("\nEnter values for first number:\n");
        for(int i=0;i<N;i++){
            arr[0]=sc.nextInt();
            A[i] = new ComplexExpression(arr, N);
            arr = new int[N]; // needed becoz of referencing issues
        }
        sc.close();
        ButterflyFFT(A,N); // N is the FFT size, get the raix base from A[] elements
        
        /* For FFT method
        A = FFT(A, N); for(int i=0;i<N;i++) System.out.println(A[i]);  
        System.out.print("\nEnter values for second number:\n");
        for(int i=0;i<N;i++){
            arr[0]=sc.nextInt();
            A[i] = new ComplexExpression(arr, N);
            arr = new int[N]; // needed becoz of referencing issues
        }
        
        ComplexExpression []fftB = FFT(A,N);

        for(int i=0;i<N;i++) {
            //System.out.print(fftA[i] + "\t"+fftB[i] + "\t");
            fftA[i].mul(fftB[i]);
            //System.out.println(fftA[i]);    
        }     
        //Calc Product FFT inverse
        // 1. replace w by it's inverse
        // find FFT then divide by N
        for(int i=0;i<N;i++){
            for(int j=1;j<=N/4;j++){
                int temp = fftA[i].COEFF[j];
                fftA[i].COEFF[j] = -fftA[i].COEFF[N/2-j];
                fftA[i].COEFF[N/2-j] = -temp; 
            }
            //System.out.println(fftA[i]);
        }
        fftA = FFT(fftA,N);
        for(int i=0;i<N;i++) {
            System.out.println(fftA[i].COEFF[0]);    
        }
        */

        
    }

    static int nextInSeq(int x, int L){ // L is bit length, FFT next sequence
        int y=1<<(L-1);
        x = x^y;
        while(x<y){
            y >>=1;
            x ^=y;
        }
        return x;
    }

    static void ButterflyFFT(ComplexExpression A[], int N){
        System.out.println("=====================");
        for(ComplexExpression c: A) System.out.println(c);

        System.out.println("=====================");
        
        int []tmparr; 
        
                    
        int L=N;                                                    //N >=2 and N = 2^k
        int M=1;
        int noOfBits=0;
        while(M!=N){
            
            for(int m=0;m<M;m++){
                int i=m*L;
                int k=0;
                ComplexExpression tmp;
                while(k++ < L/2) {
                    tmp = new ComplexExpression(A[i].COEFF,A[0].BASE); //needed for referencing issues
                    A[i].add(A[i+L/2]);
                    tmp.sub(A[i+L/2]);
                    A[i+L/2] = tmp;   
                    i++;
                }
            }
            
            tmparr = new int[N];
            tmparr[M] = 1;
            ComplexExpression w = new ComplexExpression(tmparr, N);
            
            for(int m=0;m<M;m++){
            
                int i=m*L+L/2+1;
                int k=1;                    // w^0 =1 so start from 1
                ComplexExpression ww = new ComplexExpression(w.COEFF, N); //error point, needs to be fresh for every mth iteration
                while(k++<L/2){
                    A[i++].mul(ww);
                    ww.mul(w);
                }
            }
            L/=2;
            M*=2;
            noOfBits++;
        }
        int ind=0;
            
        do{
            System.out.println(A[ind]);
            ind = nextInSeq(ind, noOfBits);
        }while(ind !=0);
    }

    static ComplexExpression[] FFT(ComplexExpression A[], int N){ 
            /* longer version, space complexity higher, time complexity comparable to above method
             * Recursive
             */
            //System.out.println("\n"+N +"-point FFT for :");
            //for(ComplexExpression c:A)System.out.println(c);

            int Nb2 = N/2;
            int base = A[0].BASE; // to be kept same otherwise scaling needed
            ComplexExpression brr[][] = new ComplexExpression[Nb2][2];
            
            //Stage 0 make the complex matrix
            for(int i=0;i<2;i++){
                int tmp = Nb2*i;
                for(int j=0;j<Nb2;j++){
                    brr[j][i] = A[j+tmp];
                }
            }
            ComplexExpression crr[] = new ComplexExpression[N];
                
            if(N==2){
                ComplexExpression tmp = new ComplexExpression(brr[0][0].COEFF,base);
                brr[0][0].add(brr[0][1]);
                tmp.sub(brr[0][1]);
                brr[0][1] = tmp;
                
            } else {      
                
                //Stage 1 FFT horizontal 2 point FFT
                for(int i=0;i<Nb2;i++)
                    brr[i] = FFT(new ComplexExpression[]{brr[i][0], brr[i][1]}, 2);
                

                //Stage 2 multiply omega
                
                int tmparr[] = new int[base];
                tmparr[base/N] =1; // w^2 (base=8, N=4)

                ComplexExpression w = new ComplexExpression(tmparr,base);
                ComplexExpression ww = new ComplexExpression(w.COEFF, base);
                
                //System.out.println("===================");
                for(int i=1;i<2;i++){
                    for(int j=1;j<Nb2;j++){
                        //System.out.println(ww);
                        brr[j][i].mul(ww);
                        ww.mul(w);

                    }
                }

                //Stage 3 FFT of N/2 points
                
                ComplexExpression C1[] = new ComplexExpression[Nb2];
                ComplexExpression C2[] = new ComplexExpression[Nb2];
                for(int i=0;i<Nb2;i++){
                    C1[i] = brr[i][0];
                    C2[i] = brr[i][1];
                }
                //System.out.println("\nSplitting to "+Nb2+"-point FFT");
                C1 = FFT(C1,Nb2);
                C2 = FFT(C2,Nb2);
                
                for(int i=0;i<Nb2;i++){ 
                    //error point, setting brr is needed for referencing issues
                    brr[i][0] = C1[i];
                    brr[i][1] = C2[i];
                }
                
            }
            //System.out.println("Result for "+N+"-point FFT");
                for(int i=0,k=0;i<Nb2;i++){
                    for(int j=0;j<2;j++){
                        crr[k++] = brr[i][j];
                        //System.out.println(crr[k-1]);
                    }
                }
                //System.out.println("==========================================");
            return crr; //convert to proper 1D form, then return
        }
        
    
}
