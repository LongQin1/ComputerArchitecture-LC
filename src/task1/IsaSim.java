package task1;
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
	static int reg[] = new int[4];

	// Here the first program hard coded as an array
	static int progr[] = {
			// As minimal RISC-V assembler example
			0x00200093, // addi x1 x0 2
			0x00300113, // addi x2 x0 3
			0x002081b3, // add x3 x1 x2
	};

	public static void main(String[] args) {

		System.out.println("Hello RISC-V World!");

		pc = 0;

		for (;;) {
			// instructions is 32bits totally 
			int instr = progr[pc];
			int opcode = instr & 0x7f;
			int rd = (instr >> 7) & 0x01f;
			int rs1 = (instr >> 15) & 0x01f;
			int imm110 = (instr >> 20);
			int imm1210 = (instr>> 25 );
			int imm115 = (instr>> 25 );
			int imm40 = (instr>>7)& 0x01f;
			int imm41 = (instr>>7) &0x01f;
			int rs2 = (instr>> 20) &  0x01f;
			int funct3=(instr>> 12) & 0x06f;
			int imm3112 = (instr>>12);
			int imm2010 = (instr>>12);

			switch (opcode) {
			//U-type
			//LUI
			case 0x37:
				reg[rd]=imm3112 << 12;
				break;
			// AUIPC
			case 0x17:
				reg[rd]=(imm3112 <<12) + pc;
				break;
			//J-type
			// I-type
			//ANDI
			case 0x13:
				switch(funct3){
				case 0x0:	
					reg[rd] = reg[rs1] + imm110;
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
							//not signed extended imm12, and what does it mean to do an unsigned comparison.
							reg[rd]=1;}
						else {reg[rd]=0;}
					}
					else {
						//Note, SLTIU rd, rs1, 1 sets rd to 1 if rs1 equals zero, otherwise sets rd to 0 (assembler pseudo-op SEQZ rd, rs).   ?????
						if(reg[rs1]==imm110) {
							reg[rd]=1;}
						else {
							reg[rd]=0;
							}
					}
					break;
				//XORI
				case 0x4:
					if((imm110 >> 11 )==1){
						if((reg[rs1] >> 4) ==1) {
							reg[rd] = (0xFFFFF000 + imm110 )^(0x7FFFFFE0 + reg[rs1]);
						}else {
							reg[rd] = (0xFFFFF000 + imm110 )^(reg[rs1]);
						}
					}else {
						if((reg[rs1] >> 4) ==1) {
						reg[rd] = (0xFFFFF000 + imm110 )^(0x7FFFFFE0 + reg[rs1]);
						}else {
						reg[rd] = (0xFFFFF000 + imm110 )^(reg[rs1]);
						}
					}
					break;
				
				//XORI
				case 0x6:
					if((imm110 >> 11 )==1){
						if((reg[rs1] >> 4) ==1) {
							reg[rd] = (0xFFFFF000 + imm110 )|(0x7FFFFFE0 + reg[rs1]);
						}else {
							reg[rd] = (0xFFFFF000 + imm110 )|(reg[rs1]);
						}
					}else {
						if((reg[rs1] >> 4) ==1) {
							reg[rd] = (0xFFFFF000 + imm110 )|(0x7FFFFFE0 + reg[rs1]);
						}else {
							reg[rd] = (0xFFFFF000 + imm110 )|(reg[rs1]);
						}	
					}
					break;
				case 0x07:
					if((imm110 >> 11 )==1){
						if((reg[rs1] >> 4) ==1) {
							reg[rd] = (0xFFFFF000 + imm110 )&(0x7FFFFFE0 + reg[rs1]);
						}else {
							reg[rd] = (0xFFFFF000 + imm110 )&(reg[rs1]);
						}
					}else {
						if((reg[rs1] >> 4) ==1) {
							reg[rd] = (0xFFFFF000 + imm110 )&(0x7FFFFFE0 + reg[rs1]);
						}else {
							reg[rd] = (0xFFFFF000 + imm110 )&(reg[rs1]);
						}	
					}
					break;
				
				case 0x01:
					reg[rd]= reg[rs1] << rs2;
					break;
				case 0x05:
					 
				}
				break;
			default:
				System.out.println("Opcode " + opcode + " not yet implemented");
				break;
			}

			++pc; // We count in 4 byte words
			if (pc >= progr.length) {
				break;
			}
			for (int i = 0; i < reg.length; ++i) {
				System.out.print(reg[i] + " ");
			}
			System.out.println();
		}

		System.out.println("Program exit");

	}

}
