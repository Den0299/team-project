package co.develhope.team_project;

import co.develhope.team_project.entities.Abbonamento;
import co.develhope.team_project.entities.Fumetto;
import co.develhope.team_project.entities.Utente;
import co.develhope.team_project.entities.enums.CategoriaFumettoEnum;
import co.develhope.team_project.entities.enums.PianoAbbonamentoEnum;
import co.develhope.team_project.entities.enums.RuoloUtenteEnum;
import co.develhope.team_project.repositories.AbbonamentoRepository;
import co.develhope.team_project.repositories.FumettoRepository;
import co.develhope.team_project.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class TeamProjectApplication implements ApplicationRunner {

	@Autowired
	private UtenteRepository utenteRepository;

	@Autowired
	private AbbonamentoRepository abbonamentoRepository;

	@Autowired
	private FumettoRepository fumettoRepository;


	public static void main(String[] args) {
		SpringApplication.run(TeamProjectApplication.class, args);
	}

	public void populateUtenti() {

		List<Utente> utenti = List.of(
				new Utente("Mario", "Rossi", "mario.rossi@example.com", "password123", "Via Roma 10, Milano", RuoloUtenteEnum.CLIENTE),
				new Utente("Lucia", "Bianchi", "lucia.bianchi@example.com", "securepass1", "Via Verdi 22, Torino", RuoloUtenteEnum.CLIENTE),
				new Utente("Giovanni", "Verdi", "giovanni.verdi@example.com", "mypassword8", "Via Dante 55, Firenze", RuoloUtenteEnum.ADMIN),
				new Utente("Anna", "Neri", "anna.neri@example.com", "superpass99", "Piazza Garibaldi 3, Napoli", RuoloUtenteEnum.CLIENTE),
				new Utente("Paolo", "Russo", "paolo.russo@example.com", "12345678", "Corso Umberto 15, Bologna", RuoloUtenteEnum.CLIENTE)
		);

		utenteRepository.saveAll(utenti);
	}

	public void populateAbbonamenti() {

		List<Abbonamento> abbonamenti= List.of(
				new Abbonamento(PianoAbbonamentoEnum.MENSILE),
				new Abbonamento(PianoAbbonamentoEnum.TRIMESTRALE),
				new Abbonamento(PianoAbbonamentoEnum.SEMESTRALE),
				new Abbonamento(PianoAbbonamentoEnum.ANNUALE)
		);

		abbonamentoRepository.saveAll(abbonamenti);
	}

	public void populateFumetti() {
		List<Fumetto> fumetti = List.of(
				new Fumetto(
						"Dylan Dog - L'alba dei morti viventi",
						"Tiziano Sclavi",
						"Sergio Bonelli Editore",
						"Il primo numero di Dylan Dog, il celebre indagatore dell'incubo.",
						LocalDate.of(1986, 10, 26),
						true,
						CategoriaFumettoEnum.HORROR
				),
				new Fumetto(
						"Tex Willer - Il totem misterioso",
						"Gianluigi Bonelli",
						"Sergio Bonelli Editore",
						"Una delle avventure più amate del ranger del Texas.",
						LocalDate.of(1950, 5, 10),
						false,
						CategoriaFumettoEnum.FANTASY
				),
				new Fumetto(
						"One Piece - Il ragazzo di gomma",
						"Eiichiro Oda",
						"Shueisha",
						"Luffy inizia la sua avventura per diventare il Re dei Pirati.",
						LocalDate.of(1997, 7, 19),
						true,
						CategoriaFumettoEnum.FANTASCIENZA
				),
				new Fumetto(
						"Naruto - Il ninja della foglia",
						"Masashi Kishimoto",
						"Shueisha",
						"La storia del giovane ninja Naruto Uzumaki.",
						LocalDate.of(1999, 9, 21),
						true,
						CategoriaFumettoEnum.AZIONE
				),
				new Fumetto(
						"Paperinik - Il diabolico vendicatore",
						"Guido Martina",
						"Panini Comics",
						"Il debutto dell'alter ego mascherato di Paperino.",
						LocalDate.of(1969, 6, 8),
						false,
						CategoriaFumettoEnum.SUPEREROI
				)
		);

		fumettoRepository.saveAll(fumetti);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// populateUtenti();
		populateAbbonamenti();
		populateFumetti();
	}
}
