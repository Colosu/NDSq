package main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

import net.automatalib.automata.transout.impl.compact.CompactMealy;

public class Operations {
	
	public void SqueezinessAndPColl(Graph g, int length, Vals vals) {

		if (length <= 0) {
			return;
		}

		int inputs = 0;
		int outputs = 0;
		HashMap<String, HashSet<String>> mapOtoI = new HashMap<String, HashSet<String>>();
		HashMap<String, HashSet<String>> mapItoO = new HashMap<String, HashSet<String>>();
		double TPColl = 0;
		double TPDiff = 0;
		double TNDSqI = 0;
		double TNDSqO = 0;

		CompactMealy<String, String> fsm = g.getMachine();
		int qid = fsm.getInitialState();
		FSMExplorer[] explorer = new FSMExplorer[fsm.getInputAlphabet().size()];
		String inp = "";
		int i = 0;
		for (Iterator<String> it = fsm.getInputAlphabet().iterator(); it.hasNext(); ) {
			inp = it.next();
			if (fsm.getTransition(qid, inp) != null) {
				explorer[i] = new FSMExplorer(fsm, fsm.getSuccessor(qid, inp), 1, length, mapOtoI, mapItoO, "" + fsm.getOutput(qid, inp), "" + inp);
				explorer[i].start();
			}
			i++;
		}
		for (int j = 0; j < i; j++) {
			try {
				if (explorer[j] != null) {
					explorer[j].join();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
//		for (int j = 0; j < i; j++) {
//			if (explorer[j] != null) {
//				inputs += explorer[j].getInputs();
//				outputs += explorer[j].getOuputs();
//			}
//		}
		
		inputs = mapItoO.keySet().size();
		outputs = mapOtoI.keySet().size();
		
//		inputs = ExploreFSM(fsm, qid, 0, length, mapOtoI, "");

//		double max = 0.0;
		HashSet<String> lis;
		int val = 0;
		for (Iterator<HashSet<String>> it = mapOtoI.values().iterator(); it.hasNext(); ) {
			lis = it.next();
			val = lis.size();
			TPColl += (double)(val) * (val - 1);

			TNDSqI += val * log2((double)val);
		}
		TPColl = TPColl/((double)(inputs) * (inputs - 1));
		TNDSqI = TNDSqI/((double)inputs);
		val = 0;
		for (Iterator<HashSet<String>> it = mapItoO.values().iterator(); it.hasNext(); ) {
			lis = it.next();
			val = lis.size();
			TPDiff += (double)(val) * (val - 1);

			TNDSqO += val * log2((double)val);
		}
		TPDiff = TPDiff/((double)(outputs) * (outputs - 1));
		TNDSqO = TNDSqO/((double)outputs);

		vals.setPColl(TPColl + TPDiff);

		vals.setNDSq(TNDSqI + TNDSqO);
//		vals.setSq10(log2(max));
		
		return;
	}
	
//	public int ExploreFSM(CompactMealy<String, String> fsm, int qid, int iter, int length, HashMap<String, Integer> mapOtoI, String output) {
//
//		int inputs = 0;
//		int count = 0;
//		String inp = "";
//		
//		if (iter == length) {
//			try {
//				inputs++;
//				Operations.sem.acquire();
//				if (mapOtoI.containsKey(output)) {
//					mapOtoI.put(output, mapOtoI.get(output) + 1);
//				} else {
//					mapOtoI.put(output, 1);
//				}
//				Operations.sem.release();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		} else {
//			count = 0;
//			for (Iterator<String> it = fsm.getInputAlphabet().iterator(); it.hasNext(); ) {
//				inp = it.next();
//				if (fsm.getTransition(qid, inp) != null) {
//					inputs += ExploreFSM(fsm, fsm.getSuccessor(qid, inp), iter + 1, length, mapOtoI, output + fsm.getOutput(qid, inp));
//					count++;
//				}
//			}
//			if (count == 0) {
//				try {
//					inputs++;
//					Operations.sem.acquire();
//					if (mapOtoI.containsKey(output)) {
//						mapOtoI.put(output, mapOtoI.get(output) + 1);
//					} else {
//						mapOtoI.put(output, 1);
//					}
//					Operations.sem.release();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		
//		return inputs;
//	}
	
	private double log2(double val) {
		return Math.log(val)/Math.log(2.0);
	}
	
	public static Semaphore sem = new Semaphore(1);
}
