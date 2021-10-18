package no.hvl.dat100.prosjekt.kontroll;

import no.hvl.dat100.prosjekt.TODO;
import no.hvl.dat100.prosjekt.kontroll.dommer.Regler;
import no.hvl.dat100.prosjekt.kontroll.spill.Handling;
import no.hvl.dat100.prosjekt.kontroll.spill.HandlingsType;
import no.hvl.dat100.prosjekt.kontroll.spill.Spillere;
import no.hvl.dat100.prosjekt.modell.Kort;
import no.hvl.dat100.prosjekt.modell.KortSamling;
import no.hvl.dat100.prosjekt.modell.Kortfarge;

/**
 * Klasse som for å representere en vriåtter syd-spiller. Strategien er å lete
 * gjennom kortene man har på hand og spille det første som er lovlig.
 *
 */
public class SydSpiller extends Spiller {

	/**
	 * Konstruktør.
	 * 
	 * @param spiller
	 *            posisjon for spilleren (nord eller syd).
	 */
	public SydSpiller(Spillere spiller) {
		super(spiller);
	}

	/**
	 * Metode for å implementere strategi. Strategien er å spille det første
	 * kortet som er lovlig (også en åtter selv om man har andre kort som også
	 * kan spilles). Dersom man ikke har lovlige kort å spille, trekker man om
	 * man ikke allerede har trukket maks antall ganger. I så fall sier man
	 * forbi.
	 * 
	 * @param topp
	 *            kort som ligg øverst på til-bunken.
	 */
	@Override
	public Handling nesteHandling(Kort topp) {

		Kort[] hand = getHand().getAllekort();
		KortSamling lovlige = new KortSamling();
		KortSamling attere = new KortSamling();
		Kort spill = null;

		int[] korttelling = new int[4];

		for(Kort k : hand){
			if(Regler.kanLeggeNed(k, topp)){
				switch(k.getFarge()){
					case Klover:
						korttelling[0]++;
						break;
					case Spar:
						korttelling[1]++;
						break;
					case Hjerter:
						korttelling[2]++;
						break;
					case Ruter:
						korttelling[3]++;
						break;
				}
				if(Regler.atter(k)){
					attere.leggTil(k);
				} else {
					lovlige.leggTil(k);
				}
			}
		}

		int biggestIndex = 0;
		for(int i=0;i<korttelling.length;i++){
			if(korttelling[i] > korttelling[biggestIndex]){
				biggestIndex = i;
			}
		}
		if(!lovlige.erTom()){
			Kortfarge farge = Kortfarge.Klover;
			switch(biggestIndex){
				case 0:
					farge = Kortfarge.Klover;
					break;
				case 1:
					farge = Kortfarge.Spar;
					break;
				case 2:
					farge = Kortfarge.Hjerter;
					break;
				case 3:
					farge = Kortfarge.Ruter;
					break;
			}
			for(Kort k : lovlige.getAllekort()){
				if(k.getFarge() == farge) spill = k;
			}
		} else if(!attere.erTom()){
			spill = attere.taSiste();
		}
		

		Handling handling = null;
		if(spill != null){
			handling = new Handling(HandlingsType.LEGGNED, spill);
		} else if(getAntallTrekk() < Regler.maksTrekk()){
			handling = new Handling(HandlingsType.TREKK, null);
		} else {
			handling = new Handling(HandlingsType.FORBI, null);
		}
		return handling;
	}
}
