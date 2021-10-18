package no.hvl.dat100.prosjekt.modell;

import java.util.Arrays;
import java.util.Random;

import no.hvl.dat100.prosjekt.TODO;

public class KortUtils {

	/**
	 * Sorterer en samling. Rekkefølgen er bestemt av compareTo() i Kort-klassen.
	 * 
	 * @see Kort
	 * 
	 * @param samling
	 * 			samling av kort som skal sorteres. 
	 */
	
	public static void sorter(KortSamling samling) {
		Kort[] nysamling = samling.getAllekort();
		Arrays.sort(nysamling);
		samling.fjernAlle();
		for(Kort k : nysamling){
			samling.leggTil(k);
		}
	}
	
	/**
	 * Stokkar en kortsamling. 
	 * 
	 * @param samling
	 * 			samling av kort som skal stokkes. 
	 */
	public static void stokk(KortSamling samling) {
		Kort[] allekort = samling.getAllekort();
		for(int i=0;i<allekort.length;i++){
			Random rng = new Random();
			int r = rng.nextInt(allekort.length);
			//Swap
			Kort kort = allekort[i];
			allekort[i] = allekort[r];
			allekort[r] = kort;
		}
		samling.fjernAlle();
		for(Kort k : allekort){
			samling.leggTil(k);
		}
	}
	
}
