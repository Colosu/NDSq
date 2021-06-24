package main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import net.automatalib.automata.transout.impl.compact.CompactMealy;

public class FSMExplorer extends Thread {

	public FSMExplorer(CompactMealy<String, String> fsm, int qid, int iter, int length, HashMap<String, HashSet<String>> mapOtoI, HashMap<String, HashSet<String>> mapItoO, String output, String input) {
		this.fsm = fsm;
		this.initQid = qid;
		this.initIter = iter;
		this.length = length;
		this.mapOtoI = mapOtoI;
		this.mapItoO = mapItoO;
		this.initOutput = output;
		this.initInput = input;
	}
	
	public int getInputs() {
		return inps;
	}
	
	public int getOuputs() {
		return outs;
	}
	
	public void run() {

//		int tmp[];
		ExploreFSM(initQid, initIter, initOutput, initInput.split("&")[0]);
//		inps = tmp[0];
//		outs = tmp[1];
		return;
	}


	private void ExploreFSM(int qid, int iter, String output, String input) {

//		int vals[] = {0,0};
//		int temp[] = {0,0};
//		int inputs = 0;
//		int outputs = 0;
		int count = 0;
		String inp = "";
//		boolean added = false;
//		String inputsList[];
//		String outputsList[];
		
		if (iter == length) {
			try {
//				inputsList = getNonDet(input);
//				outputsList = getNonDet(output);
//				inputs += inputsList.length * outputsList.length;
//				outputs += outputsList.length * inputsList.length;
//				inputs++;
//				outputs++;
				Operations.sem.acquire();
//				for (String out : outputsList) {
					if (mapOtoI.containsKey(output)) {
						mapOtoI.get(output).add(input);
					} else {
						HashSet<String> ls = new HashSet<String>();
						ls.add(input);
						mapOtoI.put(output, ls);
					}
//				}
//				for (String in : inputsList) {
					if (mapItoO.containsKey(input)) {
						mapItoO.get(input).add(output);
					} else {
						HashSet<String> ls = new HashSet<String>();
						ls.add(output);
						mapItoO.put(input, ls);
					}
//				}
				Operations.sem.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			count = 0;
			for (Iterator<String> it = fsm.getInputAlphabet().iterator(); it.hasNext(); ) {
				inp = it.next();
				if (fsm.getTransition(qid, inp) != null) {
					ExploreFSM(fsm.getSuccessor(qid, inp), iter + 1, output + fsm.getOutput(qid, inp), input + inp.split("&")[0]);
//					inputs += temp[0];
//					outputs += temp[1];
					count++;
				}
			}
			if (count == 0) {
				try {
//					inputsList = getNonDet(input);
//					outputsList = getNonDet(output);
//					inputs += inputsList.length * outputsList.length;
//					outputs += outputsList.length * inputsList.length;
//					inputs++;
//					outputs++;
					Operations.sem.acquire();
//					for (String out : outputsList) {
						if (mapOtoI.containsKey(output)) {
							mapOtoI.get(output).add(input);
						} else {
							HashSet<String> ls = new HashSet<String>();
							ls.add(input);
							mapOtoI.put(output, ls);
						}
//					}
//					for (String in : inputsList) {
						if (mapItoO.containsKey(input)) {
							mapItoO.get(input).add(output);
						} else {
							HashSet<String> ls = new HashSet<String>();
							ls.add(output);
							mapItoO.put(input, ls);
						}
//					}
					Operations.sem.release();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
//		vals[0] = inputs;
//		vals[1] = outputs;
//		return vals;
	}
	
//	private String[] getNonDet(String sequence) {
//		
//		int size = 0;
//		boolean pass = false;
//		size = (int)sequence.chars().filter(ch -> ch == '-').count();
//		int pot = Math.min((int)Math.pow(2, size), (int)Math.pow(2, Main.ND));
//		String seqs[] = new String[pot];
//		
//		seqs[0] = sequence;
//		int end = 1;
//		int prevEnd = 1;
//		String nev = "";
//		int epochs = 0;
//		int i = 0;
//		while (!pass && epochs < Main.ND) {
//			i = 0;
//			pass = true;
//			while(i < pot && seqs[i] != null && i < prevEnd) {
//				if (seqs[i].contains("-")) {
//					nev = seqs[i];
//					seqs[i] = nev.replaceFirst("-", "0");
//					seqs[end] = nev.replaceFirst("-", "1");
//					end++;
//					pass = false;
//				}
//				i++;
//			}
//			prevEnd = end;
//			epochs++;
//		}
//		
//		return seqs;
//	}
	
	private CompactMealy<String, String> fsm;
	private int initQid;
	private int initIter;
	private int length;
	private HashMap<String, HashSet<String>> mapOtoI;
	private HashMap<String, HashSet<String>> mapItoO;
	private String initOutput;
	private String initInput;
	private int inps;
	private int outs;
}
