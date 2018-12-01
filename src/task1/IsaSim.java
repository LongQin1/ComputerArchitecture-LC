package task1;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * RISC-V Instruction Set Simulator
 * 
 * A tiny first step to get the simulator started. Can execute just a single
 * RISC-V instruction.
 * 
 * @author Martin Schoeberl (martin@jopdesign.com)
 *
 */
public class IsaSim {
	static byte [] memory = new byte [4000000];
	static int pc;
	static int reg[] = new int[32];
    public static int compareUnsigned(long x, long y) {
        return Long.compare(x + Long.MIN_VALUE, y + Long.MIN_VALUE);
    }

    static 	ComputerCount PC = new ComputerCount();

    static int progr[];
	public static void main(String[] args) throws IOException {
		// Path path = Paths.get("");
		FileReader fileReader = new FileReader("C:\\Users\\Christian\\Desktop\\climify2\\ComputerArchitecture-LC\\src\\test3\\loop.bin");
		boolean jump;


		progr = fileReader.getIntergers();
		PC.setPC(0);
		for (;;) {
			jump = false;
			// instructions is 32bits totally 
			int instr = progr[ComputerCount.PC/4];
			int opcode = instr & 0x7f;
			int rd = (instr >> 7) & 0x01f;
			int rs1 = (instr >> 15) & 0x01f;
			int imm110 = (instr >> 20) & 0xFFF;
			int imm1210 = (instr>> 25) & 0x7f;
			int imm115 = (instr>> 25 ) & 0x7f;
			int imm40 = (instr>>7)& 0x01f;
			int imm41 = (instr>>7) &0x01f;
			int rs2 = (instr>> 20) &  0x01f;
			int funct3=(instr>> 12) & 7;
			int imm3112 = (instr>>12);
			int imm2010 = (instr>>12);
			int imm7 = (instr>> 25 & 0x7f);// imm1210
			int imm5 = (instr>>7) &0x01f; //imm41
			int imm11 = (instr>>7) &0x01;
			int imm4 = (instr>> 8) & 0x0F;
			int imm105 =(instr >> 25)& 0x3f;
			int imm12 = (instr >> 31) & 0x01;
			int imm13 = ( ((imm12<< 11) & 0x800) + ((imm11<<10) & 0x400) + ((imm105<<4) & 0x3F0) +(imm4 & 0xF))<<1;
			
			
			switch (opcode) {
                case 0x33:
                    switch(funct3){
                        case 0x00:
                            switch(imm115){

                                // add
                                case 0x0:

                                    reg[rd]=reg[rs1]+reg[rs2];
                                    break;
                                //sub
                                case 0x20:
                                    reg[rd]=reg[rs1]-reg[rs2];
                                    break;
                            }
                            break;
                        case 0x01://SLL
                            reg[rd]=reg[rs1] << (reg[rs2] & 0x1F);
                            break;
                        case 0x05://SRL and SRA
                            switch(imm115) {
                                case 0x0:
                                    reg[rd]= reg[rs1] >>(reg[rs2] & 0x1F);
                                    break;
                                case 0x20://SRA
                                    reg[rd]=reg[rs1]>>(reg[rs2]& 0x1F);
                                    break;
                            }break;
                        case 0x02://SLT
                            if(reg[rs1]<reg[rs2]) {
                                reg[rd]=1;
                            }else {
                                reg[rd]=0;
                            }
                            break;
                        case 0x03://SLTU
                            if(compareUnsigned(reg[rs1],reg[rs2])<0) {
                                reg[rd]=1;
                            }else {
                                reg[rd]=0;
                            }
                            break;
                        case 0x04://XOR case
                            reg[rd]=reg[rs1]^reg[rs2];
                            break;
                        case 0x06://or case
                            reg[rd]=reg[rs1]|reg[rs2];
                            break;
                        case 0x07:
                            reg[rd]=reg[rs1]&reg[rs2];
                            break;

                    }
                    break;

			//U-type
			//LUI
			case 0x37:
				reg[rd]=imm3112 << 12;
				break;
			// AUIPC
			case 0x17:
				reg[rd]=(imm3112 <<12) + pc;
				break;
			//J-type JAL in our case (jump and link)
			// I-type
			//ANDI
			case 0x13:
				switch(funct3){
				case 0x0:	// ADDI
                    if ((imm110 >> 11) == 1) {
                        reg[rd] =(0xFFFFF000 + imm110) + (reg[rs1]);
                    }
                    else {
                        reg[rd] = (imm110) + (reg[rs1]);
                    }
					break;
				case 0x2:
					if(reg[rs1] <imm110) {
						reg[rd]=1;		
					}else {
						reg[rd]=0;
					}
					break;
					// SLTIU
				case 0x3:
					if((imm110 >> 11) ==1){
						if(reg[rs1]< 0xFFFFF000 + imm110){ 
							//comparison with sign extended
							reg[rd]=1;}
						else {reg[rd]=0;}
					}
					else{
						//Note, SLTIU rd, rs1, 1 sets rd to 1 if rs1 equals zero, otherwise sets rd to 0 (assembler pseudo-op SEQZ rd, rs).   ?????
						if(reg[rs1] < imm110) {
							reg[rd]=1;}
						else {
							reg[rd]=0;
							}
					}
					break;

				//XORI
				case 0x4:
					if((imm110 >> 11) ==1){
						//if((reg[rs1] >> 4) ==1) {
							reg[rd] = (0xFFFFF000 + imm110 )^(reg[rs1]);
						}else {
							reg[rd] = (imm110 )^(reg[rs1]);
						}
					break;
					//}else {
						//if((reg[rs1] >> 4) ==1) {
					//	reg[rd] = (0xFFFFF000 + imm110 )^(0x7FFFFFE0 + reg[rs1]);
					//	}else {
					//	reg[rd] = (0xFFFFF000 + imm110 )^(reg[rs1]);
					//}
				//ORI
				case 0x6:
					if((imm110 >> 11 )==1){
						//if((reg[rs1] >> 4) ==1) {
							reg[rd] = (0xFFFFF000 + imm110 )|(reg[rs1]);
						}else {
							reg[rd] = (imm110 )|(reg[rs1]);
						}
					break;
				//	}else {
				//		if((reg[rs1] >> 4) ==1) {
				//			reg[rd] = (0xFFFFF000 + imm110 )|(0x7FFFFFE0 + reg[rs1]);
				//		}else {
				//			reg[rd] = (0xFFFFF000 + imm110 )|(reg[rs1]);
				//		}
				//	}
				case 0x07:
					if((imm110 >> 11 )==1){
				//		if((reg[rs1] >> 4) ==1) {
							reg[rd] = (0xFFFFF000 + imm110 )&(reg[rs1]);
						}else {
							reg[rd] = (imm110 )&(reg[rs1]);
						}
				//	}else {
				//		if((reg[rs1] >> 4) ==1) {
				//			reg[rd] = (0xFFFFF000 + imm110 )&(0x7FFFFFE0 + reg[rs1]);
				//		}else {
				//			reg[rd] = (0xFFFFF000 + imm110 )&(reg[rs1]);
				//		}
				//	}
					break;
				// SLLI
				case 0x01:
					reg[rd]= reg[rs1] << rs2;
					break;
				//SRLI and SRAI
				case 0x05:
					switch(imm115){
					case 0x00:
						reg[rd]= reg[rs1] >>> rs2;
					case 0x20:
						reg[rd]= reg[rs1] >> rs2;
					}
					break;
					 
				}
				break;


			case 0x73: // ECALL https://github.com/kvakil/venus/wiki/Environmental-Calls
				if(reg[10]==1) {
					System.out.println(reg[0x1011]);
				}else if(reg[10] == 4){
					System.out.println(reg[0x1011]);
				}else if (reg[10] == 9){ // using pointer couldn't do it rightn ow
					
				}else if( reg[10] == 10) { //ends the program
					ComputerCount.PC = progr.length*5;
				}else if(reg[10] == 11) {//prints ASCII character in a1
					System.out.println(Character.toString((char) reg[0x1011]));
				}else if(reg[10] == 17){ // we don't this end the program with return code ,try it after running program
					System.out.println(reg[0x1011]);
					ComputerCount.PC = progr.length*5;
				}else {
					System.out.println("please enter the right a0");
				}
				break;
			// start doing jump instructions
			// the first instruction will be JAL
			case 0x6F:
				reg[rd]=ComputerCount.PC + 4;
				PC.jal(imm1210);
				// jump=true;
				break;
			case 0x67:
				reg[rd]=ComputerCount.PC + 4;
				if (imm1210>>11==1){
					ComputerCount.PC=(reg[rs1]+(0xFFFFF000+imm1210)&0x7FFFFFFF);
				}else {
					ComputerCount.PC=(reg[rs1]+imm1210)&0x7FFFFFFF;
				}
				jump=true;
				break;

			case 0x63:
				switch (funct3){
					//BEQ Branch Equal
					case 0x00:
					if (reg[rs1] == reg[rs2]){
						if (imm12==1){
							ComputerCount.PC=ComputerCount.PC+(0xFFFFE000+imm13);
						}else {
							ComputerCount.PC=ComputerCount.PC+imm13;
						}
						jump=true;
					}
					break;
					//BNE Branch if not Equal
					case 0x01:
					if (reg[rs1] != reg[rs2]){
						if (imm12==1){
							ComputerCount.PC=ComputerCount.PC+(0xFFFFE000+imm13);
						}else {
							ComputerCount.PC=ComputerCount.PC+imm13;
						}
						jump=true;
					}
					break;
					
					//BLT
					case 0x04:
						if (reg[rs1] < reg[rs2]){
							if (imm12==1){
								ComputerCount.PC=ComputerCount.PC+(0xFFFFE000+imm13);
							}else {
								ComputerCount.PC=ComputerCount.PC+imm13;
							}
							jump=true;
						}
						break;
						
					// BGE
					case 0x05:
						if (reg[rs1] >= reg[rs2]){
							if (imm12==1){
								ComputerCount.PC=ComputerCount.PC+(0xFFFFE000+imm13);
							}else {
								ComputerCount.PC=ComputerCount.PC+imm13;
							}
							jump=true;
						}
						break;
					
					//BLTU
					case 0x06:
					if (compareUnsigned(reg[rs1],reg[rs2])<0){
						if (imm12==1){
							ComputerCount.PC=ComputerCount.PC+(0xFFFFE000+imm13);
						}else {
							ComputerCount.PC=ComputerCount.PC+imm13;
						}
						jump=true;
					}
					break;
					
					// BEGU
					case 0x07:
						if (compareUnsigned(reg[rs1],reg[rs2])>=0){
							if (imm12==1){
								ComputerCount.PC=ComputerCount.PC+(0xFFFFE000+imm13);
							}else {
								ComputerCount.PC=ComputerCount.PC+imm13;
							}
							jump=true;
						}
						break;
						
				}
				break;
				
				// load instructions
				case 0x3: 
					switch(funct3) {
					case 0x0: //LB
						if(imm110<0) { //negative offset
							imm110=(imm110 + 0xFFFFF000);
							if(memory[reg[rs1]+imm110] < 0) { //negative value in memory
								reg[rd] = (memory[reg[rs1]+imm110]&0xFF) + 0xFFFFFF00;
							}else {
								reg[rd] = (memory[reg[rs1]+imm110]&0xFF);
							}
						}else {// positive offset
							if(memory[reg[rs1]+imm110] < 0) {
								reg[rd] = ((memory[reg[rs1]+imm110])&0xFF)+0xFFFFFF00;
							}else {
								reg[rd] = (memory[reg[rs1]+imm110]&0xFF);

							}
							
						}
						break;
					case 0x4: //LBU
						if(imm110>>>11==1) {
							imm110=(imm110 +0xFFFFF000);
						}
						reg[rd] = (((int)memory[reg[rs1]+imm110])& 0xFF);
					
					break;
					case 0x1: //LH - load hexa
						if(imm110 >>>11 ==1) {
							imm110=(imm110 +0xFFFFFF00 );
							}
								reg[rd]=((int)memory[reg[rs1]+imm110+1]<<8) + ((int)memory[reg[rs1]+imm110]&0xFF);
						break;		
						
					
					case 0x2:  // LW
						if(imm110 >>>11 ==1) {
							imm110=(imm110 +0xFFFFFF00 );
							}
								reg[rd]=((int)memory[reg[rs1]+imm110+3]<<24) + (((int)memory[reg[rs1]+imm110]<<16)& 0xFFFFFF)
										+ (((int)memory[reg[rs1]+imm110]<<8)& 0xFFFF) + ((int)memory[reg[rs1]+imm110]&0xFF);	
						break;
					case 0x5://LHU
						if(imm110>>>11==1) {
							imm110=(imm110 +0xFFFFF000);
						}
						reg[rd]=(((int)memory[reg[rs1]+imm110]<<8)& 0xFFFF) + (((int)memory[reg[rs1]+imm110])&0xFF);
					break;
					}
					break;
			case  0x23:
				switch(funct3) {
				case 0x0: // SB- save byte
					imm110 = (imm115<<7)+imm40;
					if(imm110 >>>11 ==1) {
						imm110=(imm110 +0xFFFFFF00 );
						}
					memory[reg[rs1]+imm110] = (byte)(reg[rs2] & 0xFF);			
				break;
				case 0x1:// SH - save hax
					imm110 = (imm115<<7)+imm40;
					if(imm110 >>>11 ==1) {
						imm110=(imm110 +0xFFFFFF00 );
						}
					memory[reg[rs1]+imm110] =  (byte)(reg[rs2] & 0xFF);		
					memory[reg[rs1]+imm110+1]=(byte)((reg[rs2] >> 8 )& 0xFF);
				break;
				
				case 0x2:// SW -save word  
					imm110 = (imm115<<7)+imm40;
					if(imm110 >>>11 ==1) {
						imm110=(imm110 +0xFFFFFF00 );
						}
					memory[reg[rs1]+imm110] =  (byte)(reg[rs2] & 0xFF);	
					memory[reg[rs1]+imm110+1] = (byte)((reg[rs2] >> 8)&0xFF);	
					memory[reg[rs1]+imm110+2] = (byte)((reg[rs2] >> 16)&0xFF);	
				break;
				}
				break;
			default:
				System.out.println("Opcode " + opcode + " not yet implemented");
				break;
			}

			++pc; // We count in 4 byte words


			if (ComputerCount.PC/4 >= progr.length) {
				break;
			}
			if(!jump) {
				PC.nextInstruction();
			}
			for (int i = 0; i < reg.length; ++i) {
				System.out.print(Integer.toHexString(reg[i])+ "  ");
			}
			System.out.println();	
		}

		System.out.println("Program exit");

	}




}
