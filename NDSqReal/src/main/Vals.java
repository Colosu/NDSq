package main;

public class Vals {

	public double getPColl() {
		return PColl;
	}
	public void setPColl(double pColl) {
		PColl = pColl;
	}
	public double getNDSq() {
		return NDSq;
	}
	public void setNDSq(double sq) {
		NDSq = sq;
	}
	public double getSqTime() {
		return SqTime;
	}
	public void setSqTime(double sqTime) {
		SqTime = sqTime;
	}
	public double getOpSqTime() {
		return OpSqTime;
	}
	public void setOpSqTime(double opSqTime) {
		OpSqTime = opSqTime;
	}
	public double getExpTime() {
		return ExpTime;
	}
	public void setExpTime(double expTime) {
		ExpTime = expTime;
	}
	private double PColl = 0.0;
	private double NDSq = 0.0;
	private double SqTime = 0.0;
	private double OpSqTime = 0.0;
	private double ExpTime = 0.0;
}
