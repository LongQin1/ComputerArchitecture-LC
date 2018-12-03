package task1;

public class ComputerCount {


	static int PC;

	public static void setPC(int PC) {
		ComputerCount.PC = PC;
	}

	public void nextInstruction(){
		PC +=4;
	}
	
	
	public void jal(int i,int o) {
        System.out.println("i: "+i);
        if(o==1) {
			PC=(0xFFE00000  +i)+PC;
            System.out.println("pc:" +PC);
		}else{
			PC=(i)+PC;
		}
			
	}
	public int getPC() {
		return PC;
	}

}
