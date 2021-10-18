package no.hvl.dat100.prosjekt.kontroll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import no.hvl.dat100.prosjekt.TODO;
import no.hvl.dat100.prosjekt.kontroll.dommer.Regler;
import no.hvl.dat100.prosjekt.kontroll.spill.Handling;
import no.hvl.dat100.prosjekt.kontroll.spill.HandlingsType;
import no.hvl.dat100.prosjekt.kontroll.spill.Kontroll;
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

	ArrayList<Kort> spilteKort = new ArrayList<>();
	Kortfarge forbiFarge = null;

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
		if(!spilteKort.isEmpty() && spilteKort.get(spilteKort.size()-1) == topp){
			forbiFarge = topp.getFarge();
		} else {
			forbiFarge = null;
		}
		spilteKort.add(topp);
		Kort[] hand = getHand().getAllekort();
		KortSamling lovlige = new KortSamling();
		Kort spill = null;

		for(Kort k : hand){
			if(Regler.kanLeggeNed(k, topp)){
				lovlige.leggTil(k);
			}
		}

		Map<Kort, Integer> bestespill = new HashMap<>();
		for(Kort k : lovlige.getAllekort()){
			bestespill.put(k, rankKort(lovlige, k));
		}

		Map.Entry<Kort, Integer> bestekort = null;

		for (Map.Entry<Kort, Integer> entry : bestespill.entrySet()) {
			if(bestekort == null) bestekort = entry;
			if(entry.getValue() > bestekort.getValue()) bestekort = entry;
		}

		if(bestekort != null) spill = bestekort.getKey();

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

	public int rankKort(KortSamling hand, Kort kort){
		int rank = 0;
		for(Kort k : hand.getAllekort()){
			if(kort.getFarge() == k.getFarge() && !Regler.atter(k) && !k.equals(kort)) rank += 5;
		}
		if(forbiFarge != null && forbiFarge == kort.getFarge()) rank += 10;
		for(Kort k : spilteKort){
			if(kort.getFarge() == k.getFarge()) rank += 1;
			if(k.getVerdi() == kort.getVerdi()) rank += 1;
		}
		if(Regler.atter(kort)) rank = 1;
		return rank;
	}
}
