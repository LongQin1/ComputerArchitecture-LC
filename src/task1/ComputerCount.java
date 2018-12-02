package task1;

public class ComputerCount {


	static int PC;

	public static void setPC(int PC) {
		ComputerCount.PC = PC;
	}

	public void nextInstruction(){
		PC +=4;
	}
	
	
	public void jal(int i) {
	    if(i>>11==1) {
			PC=(0xFFFFF000 +i)+PC;
		}else{
			PC=(i)+PC;
		}
			
	}
	public int getPC() {
		return PC;
	}

}
