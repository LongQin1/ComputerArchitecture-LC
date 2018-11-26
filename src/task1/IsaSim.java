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

	static int pc;
	static int reg[] = new int[32];
    public static int compareUnsigned(long x, long y) {
        return Long.compare(x + Long.MIN_VALUE, y + Long.MIN_VALUE);
    }

    static 	ComputerCount PC = new ComputerCount();

    static int progr[];
	public static void main(String[] args) throws IOException {
		Path path = Paths.get("./src/cn/t3.bin");
		byte[] data = Files.readAllBytes(path);
		System.out.println("Hello RISC-V World!");

		pc = 0;
		Translator translator = new Translator(data);
		progr = translator.getFinalInstructions();

		for (;;) {
			// instructions is 32bits totally 
			int instr = progr[pc];
			int opcode = instr & 0x7f;
			int rd = (instr >> 7) & 0x01f;
			int rs1 = (instr >> 15) & 0x01f;
			int imm110 = (instr >> 20) & 0xFFF;
			int imm1210 = (instr>> 25 & 0x7f);
			int imm115 = (instr>> 25 ) & 0x7f;
			int imm40 = (instr>>7)& 0x01f;
			int imm41 = (instr>>7) &0x01f;
			int rs2 = (instr>> 20) &  0x01f;
			int funct3=(instr>> 12) & 7;
			int imm3112 = (instr>>12);
			int imm2010 = (instr>>12);
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
					System.exit(1);
				}else if(reg[10] == 11) {//prints ASCII character in a1
					System.out.println(reg[0x1011]);
				}else if(reg[10] == 17){ // we don't this end the program with return code ,try it after running program
					System.exit(reg[0x1011]);
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




			default:
				System.out.println("Opcode " + opcode + " not yet implemented");
				break;
			}

			++pc; // We count in 4 byte words
			if (pc >= progr.length) {
				break;
			}
			for (int i = 0; i < reg.length; ++i) {
				System.out.print(Integer.toHexString(reg[i])+ "  ");
			}
			System.out.println();	
		}

		System.out.println("Program exit");

	}




}
