package no.hvl.dat100.prosjekt.utsyn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import no.hvl.dat100.prosjekt.kontroll.dommer.Regler;
import no.hvl.dat100.prosjekt.kontroll.spill.Kontroll;

public class AutospillButtonListener implements ActionListener {

	private Utsyn utsyn;
	private JTextField txt;

	public AutospillButtonListener(Utsyn utsyn, JTextField txt) {
		this.utsyn = utsyn;
		this.txt = txt;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		utsyn.getLogger().info("");

		Kontroll kontroll = utsyn.getKontroll();
		int nrunder = Integer.parseInt(txt.getText());
		for(int i=1;i<=nrunder;i++){
			kontroll.startSpill();
			kontroll.spillAuto();
		}
		
		utsyn.oppdater();
		
	}

}
