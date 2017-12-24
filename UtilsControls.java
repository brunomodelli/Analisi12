class UtilsControls
{
	static float controlMonth(Calendar cal,int month,float bank,float betSize,FinalEntry i,int succ,int alls)
	{
		if (cal.get(Calendar.MONTH) == month) 
		{
			float gain = i.prediction >= i.upper ? i.fixture.maxOver : i.fixture.maxUnder;
			bank += betSize * (i.success() ? (gain - 1f) : -1f);
			succ += i.success() ? 1 : 0;
			alls++;
		}
	}
	static float controlNotMonth(Calendar cal,int month,float bank,float betSize,FinalEntry i,int succ,int alls,float previous,float percent)
	{
		if (cal.get(Calendar.MONTH) != month)  
		{
			System.out.println("Bank after month: " + (month + 1) + " is: " + bank + " unit: " + betSize
					+ " profit: " + (bank - previous) + " in units: " + (bank - previous) / betSize + " rate: "
					+ (float) succ / alls + "%");
			previous = bank;
			betSize = bank * percent;
			month = cal.get(Calendar.MONTH);
			float gain = i.prediction >= i.upper ? i.fixture.maxOver : i.fixture.maxUnder;
			bank += betSize * (i.success() ? (gain - 1f) : -1f);
			alls = 1;
			succ = i.success() ? 1 : 0;
		}
	}
	
	static float controlAvg(float avgShotsUnder,float avgShotsOver)
	{
		if (avgShotsUnder > avgShotsOver) 
		{
			return 0.5f;
		}
		return 0f;
	}
	static float controlExpectedGreater(float expected,float avgShotsUnder,float avgShotsOver,float dist)
	{
		if (expected >= avgShotsOver && expected > avgShotsUnder) {
			float score = 0.5f + 0.5f * (expected - avgShotsOver) / dist;
			return (score >= 0 && score <= 1f) ? score : 1f;
		}
		return 0f;
	}
	static float controlExpectedSmaller(float expected,float avgShotsUnder,float avgShotsOver,float dist)
	{
		if ((expected >= avgShotsOver && expected > avgShotsUnder) && expected <= avgShotsUnder && expected < avgShotsOver) {
			float score = 0.5f - 0.5f * (-expected + avgShotsUnder) / dist;
			return (score >= 0 && score <= 1f) ? score : 0f;
		} else 
		{
			// System.out.println(f);
			return 0.5f;
		}
	}
	
	static float controlNewMaxBy1(MaximizingBy newMaxBy,ArrayList<HTEntry> all,float currentProfit,float currEval)
	{
		if (newMaxBy.equals(MaximizingBy.BOTH) && all.size() >= 100) {
			currentProfit = getProfitHT(all);
			currEval = evaluateRecord(getFinals(all));
		}
	}
	static float controlNewMaxBy2(MaximizingBy newMaxBy,ArrayList<HTEntry> all,float currentProfit,float currEval)
	{
		if (!newMaxBy.equals(MaximizingBy.BOTH) && newMaxBy.equals(MaximizingBy.UNDERS) && onlyUnders(getFinals(all)).size() >= 100) {
			currentProfit = getProfitHT(onlyUndersHT(all));
			currEval = evaluateRecord(onlyUnders(getFinals(all)));
		}
	}
	static float controlNewMaxBy3(MaximizingBy newMaxBy,ArrayList<HTEntry> all,float currentProfit,float currEval)
	{
		if (newMaxBy.equals(MaximizingBy.OVERS) && onlyOvers(getFinals(all)).size() >= 100) {
			currentProfit = getProfitHT(onlyOversHT(all));
			currEval = evaluateRecord(onlyOvers(getFinals(all)));
		} else {
			currentProfit = Float.NEGATIVE_INFINITY;
		}
	}
	static float controlCurrEval(float currEval,float bestEval,float bestProfit,float currentProfit,
			float bestWinRatio,float currentWinRate,float bestx,float step,int x,float besty,int y,float bestz,int z,float bestw,int w,
			float bestTH,float currentTH,String bestDescription)
	{
		if (/* currentProfit > bestProfit */ currEval > bestEval/*
				 * currentWinRate
				 * >
				 * bestWinRatio
				 */) {
			bestProfit = currentProfit;
			bestEval = currEval;
			bestWinRatio = currentWinRate;
			bestx = step * x;
			besty = step * y;
			bestz = step * z;
			bestw = step * w;
			bestTH = currentTH;
			bestDescription = x * step + "*zero + " + y * step + "*one + " + z * step + " *two+ "
					+ w * step + " *>=3";
			// System.out.println(bestProfit);
			// System.out.println("1 in " + bestEval);
		}
		return bestEval;
	}

}