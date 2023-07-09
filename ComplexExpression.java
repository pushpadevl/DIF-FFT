public class ComplexExpression{ //Complex expression
    /* 1 + w + 2w^2 type
     * So when multiplying a polynomial to this
     * it can have wraparounds, but the BASE remains same
    */
    int []COEFF;                //coeff wraparound when size exceeded
    int BASE;                   // give sizes of 2 power, like 4 8 16
    
    ComplexExpression(int[] coeff, int base){
        BASE = base;
        int B2 = BASE/2;
        for(int i=0;i<B2;i++){
            coeff[i] -= coeff[i+B2];
            coeff[i+B2] = 0;
        }
        COEFF = coeff.clone();

    }
    
    void mul(ComplexExpression p){
        /* possible improvement 
         * use register set and rotate right with wraparound
         * Code for different bases 
         */ 
        int coeff[] = new int[BASE];
        
        for(int i=0;i<BASE;i++){
            for(int j=0;j<BASE;j++){
                coeff[(i+j)%BASE] += COEFF[i] * p.COEFF[j];
            }
        }
        int B2 = BASE/2;
        for(int i=0;i<B2;i++){
            coeff[i] -= coeff[i+B2];
            coeff[i+B2]=0;
        }
        COEFF = coeff.clone();
    }

    void add(ComplexExpression p){
        for(int i=0;i<BASE;i++){
            this.COEFF[i] += p.COEFF[i];
        }
    }

    void sub(ComplexExpression p){
        for(int i=0;i<BASE;i++){
            this.COEFF[i] -= p.COEFF[i];
        }
    }
    
    @Override
    public String toString(){
        int i=-1;                               //start index
        while((i<BASE) && (COEFF[++i]==0));
        int j=BASE;                             //end index
        while((j>-1)&&(COEFF[--j]==0));

        int B2=BASE/2;                          //Negative wraparounds w^1 = -w^5 in base 8
        StringBuffer sb = new StringBuffer("[");
        
        boolean isPrevNZ = false;

        if(i==0) {
            isPrevNZ = true;
            sb.append(" " + COEFF[i++]);
        }
        if(i==1 && COEFF[i] != 0){
            if(isPrevNZ){
                if( COEFF[i]>0 ) sb.append(" +"+COEFF[i]+"w");
                else sb.append(" "+COEFF[i]+"w");
            }else{
                sb.append(" "+COEFF[i]+"w");
            } 
            isPrevNZ = true;
            i++;
        }

        for(;i<=j;i++){
            if(COEFF[i]!=0){
                if(isPrevNZ){
                    if(COEFF[i]>0) sb.append(" +" + COEFF[i]+"w^"+(i));
                    else sb.append(" "+COEFF[i]+"w^"+(i));
                }else {
                    sb.append(" "+COEFF[i]+"w^"+(i));
                }
            }
        }
        sb.append(" ]");
    
        return sb.toString();
    }
    
    public static void main(String[] args) {
        int arr[] = {1,2,3,4};
        int brr[] = {5,6,7,8};
        ComplexExpression A = new ComplexExpression(arr, 4);
        ComplexExpression B = new ComplexExpression(brr,4);
        //System.out.println(A);
        //System.out.println(B);
        B.add(A);
        //System.out.println(B);
        int tmparr[] = new int[8];
        tmparr[2] =1;
        ComplexExpression w = new ComplexExpression(tmparr,8);
            
        ComplexExpression ww = new ComplexExpression(w.COEFF, w.BASE); //works
        ww.COEFF[2] = 0; ww.COEFF[1] = -4; ww.COEFF[3] = 4;
        //ww.COEFF = w.COEFF.clone(); //works
        ww.mul(w);
        System.out.println(ww);
        for(int i=0;i<2;i++){
            
            for(int j=0;j<4;j++){
                //System.out.println(ww);
                ww.mul(w);
            }
        }

    }
}
