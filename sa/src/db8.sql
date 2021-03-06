--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.15
-- Dumped by pg_dump version 9.5.15

-- Started on 2019-01-22 10:09:05

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2261 (class 1262 OID 16489)
-- Name: sss; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE sss WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Italian_Italy.1252' LC_CTYPE = 'Italian_Italy.1252';


ALTER DATABASE sss OWNER TO postgres;

\connect sss

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2 (class 3079 OID 12355)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2264 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- TOC entry 1 (class 3079 OID 16490)
-- Name: adminpack; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;


--
-- TOC entry 2265 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION adminpack; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';


--
-- TOC entry 227 (class 1255 OID 16791)
-- Name: assegna_docente(integer, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.assegna_docente(matricola_fornita integer, materia character varying) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
	INSERT INTO tiene (docente, codice_corso)
	SELECT matricola, codice_corso
	FROM docente, corso
	WHERE corso.nome = materia AND docente.matricola = matricola_fornita; 
END;
$$;


ALTER FUNCTION public.assegna_docente(matricola_fornita integer, materia character varying) OWNER TO postgres;

--
-- TOC entry 2266 (class 0 OID 0)
-- Dependencies: 227
-- Name: FUNCTION assegna_docente(matricola_fornita integer, materia character varying); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.assegna_docente(matricola_fornita integer, materia character varying) IS 'Assegna un corso di competenza ad un docente';


--
-- TOC entry 226 (class 1255 OID 16790)
-- Name: assegna_studenti(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.assegna_studenti(matricola_fornita integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
	INSERT INTO iscritto_a (matricola, codice_corso)
	SELECT matricola, codice_corso
	FROM studente, corso
	WHERE matricola = matricola_fornita AND facolta IN (SELECT corso_laurea
			   		FROM studente
			   		WHERE matricola = matricola_fornita);
END;
$$;


ALTER FUNCTION public.assegna_studenti(matricola_fornita integer) OWNER TO postgres;

--
-- TOC entry 2267 (class 0 OID 0)
-- Dependencies: 226
-- Name: FUNCTION assegna_studenti(matricola_fornita integer); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.assegna_studenti(matricola_fornita integer) IS 'Assegna i corsi di competenza di uno studente al suo piano di studi';


--
-- TOC entry 214 (class 1255 OID 16736)
-- Name: controllo_mail(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.controllo_mail(mail character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$

BEGIN
	IF (EXISTS (SELECT *
		   FROM utente
		   WHERE email = mail))
	THEN RETURN TRUE;
	ELSE RETURN FALSE;
	END IF;
END;

$$;


ALTER FUNCTION public.controllo_mail(mail character varying) OWNER TO postgres;

--
-- TOC entry 242 (class 1255 OID 16854)
-- Name: crea_utente(character varying, character varying, character varying, smallint, integer, character varying, character varying, character varying, integer, character varying, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.crea_utente(p_nome character varying, p_cognome character varying, p_email character varying, p_tipo_utente smallint, p_anno_immatricolazione integer, p_corso_laurea character varying, p_stato_carriera character varying, p_struttura_riferimento character varying, p_matricola integer, p_random_password character varying, p_random_codatt integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
        BEGIN

        INSERT INTO utente (matricola, nome, cognome, email, tipo_utente, password_hash, codice_attivazione)
                    VALUES (p_matricola, p_nome, p_cognome, p_email, p_tipo_utente, MD5(p_random_password), p_random_codatt);
        
        IF p_tipo_utente = 1 THEN
            INSERT INTO studente (matricola, anno_immatricolazione, corso_laurea, stato_carriera)
                    VALUES (p_matricola, p_anno_immatricolazione, p_corso_laurea, p_stato_carriera);
        ELSIF p_tipo_utente = 2 THEN
            INSERT INTO docente (matricola, struttura_riferimento)
                    VALUES (p_matricola, p_struttura_riferimento);
        ELSIF p_tipo_utente = 3 THEN
            INSERT INTO amministratore (matricola, struttura_riferimento)
                    VALUES (p_matricola, p_struttura_riferimento);
        END IF;
                            
        END; $$;


ALTER FUNCTION public.crea_utente(p_nome character varying, p_cognome character varying, p_email character varying, p_tipo_utente smallint, p_anno_immatricolazione integer, p_corso_laurea character varying, p_stato_carriera character varying, p_struttura_riferimento character varying, p_matricola integer, p_random_password character varying, p_random_codatt integer) OWNER TO postgres;

--
-- TOC entry 224 (class 1255 OID 16784)
-- Name: elimina_dati_corsi(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.elimina_dati_corsi(codice integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
	DELETE FROM corso
	WHERE codice_corso = codice;
END;
$$;


ALTER FUNCTION public.elimina_dati_corsi(codice integer) OWNER TO postgres;

--
-- TOC entry 245 (class 1255 OID 16884)
-- Name: get_accessi_by_corso(character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_accessi_by_corso(p_date_start character varying, p_date_end character varying) RETURNS TABLE(cod_corso smallint, conteggio_accesi integer)
    LANGUAGE plpgsql
    AS $$
      BEGIN
         RETURN QUERY SELECT codice_corso,
                             CAST(COUNT(matricola) AS INTEGER)
                      FROM accesso_corso
                      WHERE inizio_accesso BETWEEN to_timestamp(p_date_start, 'YYYY-MM-DD HH24:MI') AND to_timestamp(p_date_end, 'YYYY-MM-DD HH24:MI')
                      GROUP BY codice_corso;
      END; $$;


ALTER FUNCTION public.get_accessi_by_corso(p_date_start character varying, p_date_end character varying) OWNER TO postgres;

--
-- TOC entry 229 (class 1255 OID 16800)
-- Name: get_dati_docente(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_dati_docente(mat integer) RETURNS TABLE(matricola integer, nome character varying, cognome character varying, email character varying, tipo_utente smallint, struttura_riferimento character varying)
    LANGUAGE plpgsql
    AS $$

BEGIN
	RETURN QUERY
	SELECT utente.matricola, utente.nome,
	 utente.cognome, utente.email, utente.tipo_utente,  
	docente.struttura_riferimento 
	FROM utente JOIN docente ON (utente.matricola = docente.matricola)
	WHERE utente.matricola = mat;
END;

$$;


ALTER FUNCTION public.get_dati_docente(mat integer) OWNER TO postgres;

--
-- TOC entry 2268 (class 0 OID 0)
-- Dependencies: 229
-- Name: FUNCTION get_dati_docente(mat integer); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.get_dati_docente(mat integer) IS 'Fornisce i dati utili di un docente, partendo dalla matricola';


--
-- TOC entry 243 (class 1255 OID 16863)
-- Name: get_dati_utente(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_dati_utente(p_matricola integer) RETURNS TABLE(matricola integer, nome character varying, cognome character varying, email character varying, tipo_utente smallint, anno_immatricolazione integer, corso_laurea character varying, stato_carriera character varying, struttura_riferimento character varying)
    LANGUAGE plpgsql
    AS $$
    BEGIN
        RETURN QUERY SELECT
                        utente.matricola,
                        utente.nome,
                        utente.cognome,
                        utente.email,
                        utente.tipo_utente,
                        studente.anno_immatricolazione,
                        studente.corso_laurea,
                        studente.stato_carriera,
                        docente.struttura_riferimento
                     FROM
                        utente
                     LEFT JOIN studente ON studente.matricola = utente.matricola
                     LEFT JOIN docente ON docente.matricola = utente.matricola
                     WHERE
                        utente.matricola = p_matricola AND docente.struttura_riferimento IS NOT NULL
                     
                     UNION

                     SELECT
                        utente.matricola,
                        utente.nome,
                        utente.cognome,
                        utente.email,
                        utente.tipo_utente,
                        studente.anno_immatricolazione,
                        studente.corso_laurea,
                        studente.stato_carriera,
                        amministratore.struttura_riferimento
                     FROM
                        utente
                     LEFT JOIN studente ON studente.matricola = utente.matricola
                     LEFT JOIN docente ON docente.matricola = utente.matricola
                     LEFT JOIN amministratore ON amministratore.matricola = utente.matricola
                     WHERE
                        utente.matricola = p_matricola AND amministratore.struttura_riferimento IS NOT NULL

                     UNION

                      SELECT
                        utente.matricola,
                        utente.nome,
                        utente.cognome,
                        utente.email,
                        utente.tipo_utente,
                        studente.anno_immatricolazione,
                        studente.corso_laurea,
                        studente.stato_carriera,
                        amministratore.struttura_riferimento
                     FROM
                        utente
                     LEFT JOIN studente ON studente.matricola = utente.matricola
                     LEFT JOIN docente ON docente.matricola = utente.matricola
                     LEFT JOIN amministratore ON amministratore.matricola = utente.matricola
                     WHERE
                        utente.matricola = p_matricola AND studente.anno_immatricolazione IS NOT NULL;
    END; $$;


ALTER FUNCTION public.get_dati_utente(p_matricola integer) OWNER TO postgres;

--
-- TOC entry 240 (class 1255 OID 16836)
-- Name: get_downloads_corsi(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_downloads_corsi() RETURNS TABLE(cod_corso integer, conteggio_download integer)
    LANGUAGE plpgsql
    AS $$
      BEGIN
         RETURN QUERY SELECT CAST(codice_corso as integer),
                             CAST(COUNT(download.matricola) as INTEGER)
                      FROM download
                      JOIN risorsa ON risorsa.codice_risorsa = download.codice_risorsa
                      JOIN sezione ON sezione.codice_sezione = risorsa.codice_sezione
                      GROUP BY codice_corso;
      END; $$;


ALTER FUNCTION public.get_downloads_corsi() OWNER TO postgres;

--
-- TOC entry 237 (class 1255 OID 16826)
-- Name: get_downloads_intervallo(integer, character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_downloads_intervallo(p_codice_corso integer, p_start_date character varying, p_end_date character varying) RETURNS TABLE(codice_risorsa integer, conteggio_download integer)
    LANGUAGE plpgsql
    AS $$
      BEGIN
         RETURN QUERY SELECT download.codice_risorsa,
                             CAST(COUNT(DISTINCT(download.matricola)) AS INTEGER)
                      FROM download
                      JOIN risorsa ON risorsa.codice_risorsa = download.codice_risorsa
                      JOIN sezione ON sezione.codice_sezione = risorsa.codice_sezione
                      WHERE codice_corso = p_codice_corso AND data_e_ora BETWEEN to_timestamp(p_start_date, 'YYYY-MM-DD HH24:MI') AND to_timestamp(p_end_date, 'YYYY-MM-DD HH24:MI')
                      GROUP BY download.codice_risorsa;
      END; $$;


ALTER FUNCTION public.get_downloads_intervallo(p_codice_corso integer, p_start_date character varying, p_end_date character varying) OWNER TO postgres;

--
-- TOC entry 215 (class 1255 OID 16742)
-- Name: get_matricola_from_mail(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_matricola_from_mail(mail_utente character varying) RETURNS TABLE(matricola integer)
    LANGUAGE plpgsql
    AS $$

BEGIN 
	RETURN QUERY
	SELECT utente.matricola
	FROM utente
	WHERE utente.email = mail_utente;
END;

$$;


ALTER FUNCTION public.get_matricola_from_mail(mail_utente character varying) OWNER TO postgres;

--
-- TOC entry 2269 (class 0 OID 0)
-- Dependencies: 215
-- Name: FUNCTION get_matricola_from_mail(mail_utente character varying); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.get_matricola_from_mail(mail_utente character varying) IS 'Restituisce il result set contenente la matricola dell''utente con mail corrispondente a quella passata come parametro in ingresso della funzione';


--
-- TOC entry 239 (class 1255 OID 16833)
-- Name: get_tempo_medio_corsi(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_tempo_medio_corsi() RETURNS TABLE(cod_corso integer, tempo_medio integer)
    LANGUAGE plpgsql
    AS $$
       BEGIN
          RETURN QUERY SELECT codice_corso, CAST(ceil(AVG((DATE_PART('day', fine_accesso - inizio_accesso) * 24 * 60 + 
                                                 DATE_PART('hour', fine_accesso - inizio_accesso)) * 60 +
                                                 DATE_PART('minute', fine_accesso - inizio_accesso))) as integer)
                        FROM accesso_corso
                        WHERE fine_accesso IS NOT NULL
                        GROUP BY codice_corso;
       END; $$;


ALTER FUNCTION public.get_tempo_medio_corsi() OWNER TO postgres;

--
-- TOC entry 238 (class 1255 OID 16830)
-- Name: get_tempo_medio_corso(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_tempo_medio_corso(p_codice_corso integer) RETURNS TABLE(tempo_medio integer)
    LANGUAGE plpgsql
    AS $$
       BEGIN
          RETURN QUERY  SELECT cast(ceil(AVG((DATE_PART('day', fine_accesso - inizio_accesso) * 24 * 60 + 
                               DATE_PART('hour', fine_accesso - inizio_accesso)) * 60 +
                               DATE_PART('minute', fine_accesso - inizio_accesso))) as integer)
                        FROM accesso_corso
                        WHERE fine_accesso IS NOT NULL AND codice_corso = p_codice_corso;
       END; $$;


ALTER FUNCTION public.get_tempo_medio_corso(p_codice_corso integer) OWNER TO postgres;

--
-- TOC entry 210 (class 1255 OID 16745)
-- Name: getcontenutocorso(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getcontenutocorso(o_cod integer) RETURNS TABLE(codice_sezione smallint, descrizione character varying, titolo character varying, is_pubblica boolean, cod_corso smallint, figlio_di smallint, matricola integer)
    LANGUAGE plpgsql
    AS $$
    BEGIN
        RETURN QUERY SELECT
                        sezione.codice_sezione,
                        sezione.descrizione,
                        sezione.titolo,
                        sezione.is_pubblica, 
                        sezione.codice_corso, 
                        sezione.figlio_di, 
                        sezione.matricola
                     FROM
                        sezione
                     WHERE
                         sezione.codice_corso = o_cod;
    END; $$;


ALTER FUNCTION public.getcontenutocorso(o_cod integer) OWNER TO postgres;

--
-- TOC entry 212 (class 1255 OID 16746)
-- Name: getcontenutocorso1(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getcontenutocorso1(o_cod integer) RETURNS TABLE(codice_risorsa integer, nome character varying, descrizione character varying, percorso character varying, tipo character varying, codice_sezione smallint, is_pubblica boolean)
    LANGUAGE plpgsql
    AS $$
    BEGIN
        RETURN QUERY SELECT 
        r.codice_risorsa, 
        r.nome, 
        r.descrizione, 
        r.percorso, 
        r.tipo, 
        r.codice_sezione, 
        r.is_pubblica
	FROM risorsa r JOIN sezione s 
	ON (r.codice_sezione = s.codice_sezione)
	WHERE r.codice_sezione=o_cod;
    END; $$;


ALTER FUNCTION public.getcontenutocorso1(o_cod integer) OWNER TO postgres;

--
-- TOC entry 219 (class 1255 OID 16756)
-- Name: getcorsi(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getcorsi() RETURNS TABLE(codice_corso smallint, nome character varying, anno_attivazione smallint, facolta character varying, descrizione character varying, peso smallint, creatore integer)
    LANGUAGE plpgsql
    AS $$
	BEGIN
        RETURN QUERY
		SELECT c.codice_corso,
			c.nome,
			c.anno_attivazione,
			c.facolta,
			c.descrizione,
			c.peso,
			c.creatore
		FROM corso c;
	END;
$$;


ALTER FUNCTION public.getcorsi() OWNER TO postgres;

--
-- TOC entry 222 (class 1255 OID 16754)
-- Name: getcorsiutente(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getcorsiutente(mat integer) RETURNS TABLE(nome character varying)
    LANGUAGE plpgsql
    AS $$
	BEGIN
	IF mat IN (SELECT matricola
			FROM utente
			WHERE tipo_utente=1)
	THEN
        RETURN QUERY
		SELECT c.nome
		FROM utente u JOIN iscritto_a i
		ON(u.matricola=i.matricola)
		JOIN corso c ON (i.codice_corso=c.codice_corso)		
		WHERE mat=u.matricola;
	END IF;
	IF mat IN (SELECT matricola
			FROM utente
			WHERE tipo_utente=2)
	THEN
        RETURN QUERY
		SELECT c.nome
		FROM corso c JOIN tiene t ON(c.codice_corso=t.codice_corso)
		WHERE mat=t.docente;
	END IF;
	IF mat IN (SELECT matricola
			FROM utente
			WHERE tipo_utente=3)
	THEN
        RETURN QUERY
		SELECT c.nome
		FROM corso c
		WHERE c.creatore = mat;
	END IF;
	END;
$$;


ALTER FUNCTION public.getcorsiutente(mat integer) OWNER TO postgres;

--
-- TOC entry 244 (class 1255 OID 16864)
-- Name: getcorso(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getcorso(cod integer) RETURNS TABLE(codice_corso smallint, nome character varying, anno_attivazione smallint, facolta character varying, descrizione character varying, peso smallint, creatore integer)
    LANGUAGE plpgsql
    AS $$
	BEGIN
        RETURN QUERY
		SELECT corso.codice_corso, 
		corso.nome, 
		corso.anno_attivazione, 
		corso.facolta, 
		corso.descrizione, 
		corso.peso, 
		corso.creatore
		FROM corso
		WHERE corso.codice_corso=cod;
	END;
$$;


ALTER FUNCTION public.getcorso(cod integer) OWNER TO postgres;

--
-- TOC entry 233 (class 1255 OID 16808)
-- Name: getcorso(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getcorso(cod character varying) RETURNS TABLE(codice_corso smallint, nome character varying, anno_attivazione smallint, facolta character varying, descrizione character varying, peso smallint, creatore integer)
    LANGUAGE plpgsql
    AS $$
	BEGIN
        RETURN QUERY
		SELECT corso.codice_corso, 
		corso.nome, 
		corso.anno_attivazione, 
		corso.facolta, 
		corso.descrizione, 
		corso.peso, 
		corso.creatore
		FROM corso
		WHERE corso.nome=cod;
	END;
$$;


ALTER FUNCTION public.getcorso(cod character varying) OWNER TO postgres;

--
-- TOC entry 216 (class 1255 OID 16715)
-- Name: getemailutenti(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getemailutenti(cod integer) RETURNS TABLE(email character varying)
    LANGUAGE plpgsql
    AS $$
	BEGIN
        RETURN QUERY
		SELECT u.email
		FROM utente u JOIN iscritto_a i
		ON(u.matricola=i.matricola)
		JOIN corso c ON (i.codice_corso=c.codice_corso)		
		WHERE cod=i.codice_corso;
	END;
$$;


ALTER FUNCTION public.getemailutenti(cod integer) OWNER TO postgres;

--
-- TOC entry 232 (class 1255 OID 16805)
-- Name: getutentiregistrati(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getutentiregistrati() RETURNS TABLE(matricola integer)
    LANGUAGE plpgsql
    AS $$
	BEGIN
        RETURN QUERY
		SELECT utente.matricola
		FROM utente;
	END;
$$;


ALTER FUNCTION public.getutentiregistrati() OWNER TO postgres;

--
-- TOC entry 223 (class 1255 OID 16782)
-- Name: import_dati_corsi(integer, character varying, integer, character varying, character varying, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.import_dati_corsi(code integer, nominative character varying, a_year integer, faculty character varying, description character varying, cfu integer, creator integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
	INSERT INTO corso (codice_corso, nome, anno_attivazione, facolta, descrizione, peso, creatore)
	VALUES (code, nominative, a_year, faculty, description, cfu, creator);
END;
$$;


ALTER FUNCTION public.import_dati_corsi(code integer, nominative character varying, a_year integer, faculty character varying, description character varying, cfu integer, creator integer) OWNER TO postgres;

--
-- TOC entry 220 (class 1255 OID 16778)
-- Name: incremento_login(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.incremento_login(codice character varying) RETURNS void
    LANGUAGE plpgsql
    AS $$

BEGIN
	UPDATE utente
	SET tentativi_login = tentativi_login + 1
	WHERE email = codice;
END;

$$;


ALTER FUNCTION public.incremento_login(codice character varying) OWNER TO postgres;

--
-- TOC entry 235 (class 1255 OID 16813)
-- Name: iscrivi_studente_al_corso(integer, smallint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.iscrivi_studente_al_corso(matr integer, cod_corso smallint) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
	INSERT INTO iscritto_a (matricola, codice_corso)
	VALUES (matr, cod_corso);
END;
$$;


ALTER FUNCTION public.iscrivi_studente_al_corso(matr integer, cod_corso smallint) OWNER TO postgres;

--
-- TOC entry 225 (class 1255 OID 16789)
-- Name: modifica_dati_corsi(integer, character varying, integer, character varying, character varying, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.modifica_dati_corsi(code integer, nominative character varying, a_year integer, faculty character varying, description character varying, cfu integer, creator integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
	UPDATE corso
	SET codice_corso = code, 
	nome = nominative,
	anno_attivazione = a_year, 
	facolta = faculty,
	descrizione = description,
	peso = cfu,
	creatore = creator
	WHERE codice_corso = code;
END;
$$;


ALTER FUNCTION public.modifica_dati_corsi(code integer, nominative character varying, a_year integer, faculty character varying, description character varying, cfu integer, creator integer) OWNER TO postgres;

--
-- TOC entry 2270 (class 0 OID 0)
-- Dependencies: 225
-- Name: FUNCTION modifica_dati_corsi(code integer, nominative character varying, a_year integer, faculty character varying, description character varying, cfu integer, creator integer); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.modifica_dati_corsi(code integer, nominative character varying, a_year integer, faculty character varying, description character varying, cfu integer, creator integer) IS 'Aggiorna i dati relativi al corso specificato';


--
-- TOC entry 241 (class 1255 OID 16839)
-- Name: modifica_dati_utente(character varying, character varying, character varying, smallint, smallint, character varying, character varying, character varying, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.modifica_dati_utente(p_nome character varying, p_cognome character varying, p_email character varying, p_tipo_utente smallint, p_anno_immatricolazione smallint, p_corso_laurea character varying, p_stato_carriera character varying, p_struttura_riferimento character varying, p_matricola integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
        BEGIN
            UPDATE utente SET
                            nome = p_nome,
                            cognome = p_cognome,
                            email = p_email,
                            tipo_utente = p_tipo_utente
                        WHERE
                            matricola = p_matricola;

            UPDATE studente SET
                            anno_immatricolazione = p_anno_immatricolazione,
                            corso_laurea = p_corso_laurea,
                            stato_carriera = p_stato_carriera
                        WHERE
                            matricola = p_matricola;

            UPDATE docente SET
                            struttura_riferimento = p_struttura_riferimento
                        WHERE
                            matricola = p_matricola;
            
            UPDATE amministratore SET
                            struttura_riferimento = p_struttura_riferimento
                        WHERE
                            matricola = p_matricola;
                            
                            
        END; $$;


ALTER FUNCTION public.modifica_dati_utente(p_nome character varying, p_cognome character varying, p_email character varying, p_tipo_utente smallint, p_anno_immatricolazione smallint, p_corso_laurea character varying, p_stato_carriera character varying, p_struttura_riferimento character varying, p_matricola integer) OWNER TO postgres;

--
-- TOC entry 234 (class 1255 OID 16812)
-- Name: modificarisorsa(smallint, character varying, character varying, character varying, character varying, smallint, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.modificarisorsa(codrisorsa smallint, nom character varying, descr character varying, path character varying, tipo_risorsa character varying, codsezione smallint, visibilita boolean) RETURNS void
    LANGUAGE plpgsql
    AS $$
	BEGIN
		UPDATE risorsa
		SET 
		nome = nom,
		descrizione = descr,
		percorso = path,
		tipo = tipo_risorsa,
		codice_sezione = codsezione,
		is_pubblica = visibilita
		WHERE codice_risorsa = codrisorsa;
	END;
$$;


ALTER FUNCTION public.modificarisorsa(codrisorsa smallint, nom character varying, descr character varying, path character varying, tipo_risorsa character varying, codsezione smallint, visibilita boolean) OWNER TO postgres;

--
-- TOC entry 236 (class 1255 OID 16816)
-- Name: modificasezione(integer, character varying, boolean, character varying, integer, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.modificasezione(codice integer, tit character varying, visibilita boolean, descr character varying, codcorso integer, fdi integer, mat integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
	BEGIN
		UPDATE sezione
		SET 
		titolo = tit,
		descrizione = descr,
		is_pubblica = visibilita,
		codice_corso = codcorso,
		figlio_di = fdi,
		matricola = mat
		WHERE codice_sezione = codice;
	END;
$$;


ALTER FUNCTION public.modificasezione(codice integer, tit character varying, visibilita boolean, descr character varying, codcorso integer, fdi integer, mat integer) OWNER TO postgres;

--
-- TOC entry 217 (class 1255 OID 16744)
-- Name: query_dati(character, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.query_dati(pwd character, mail character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$

BEGIN
IF (EXISTS (SELECT *
		   FROM utente
		   WHERE password_hash = pwd AND email = mail ))
THEN RETURN TRUE;
ELSE RETURN FALSE;
END IF;
END;

$$;


ALTER FUNCTION public.query_dati(pwd character, mail character varying) OWNER TO postgres;

--
-- TOC entry 2271 (class 0 OID 0)
-- Dependencies: 217
-- Name: FUNCTION query_dati(pwd character, mail character varying); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.query_dati(pwd character, mail character varying) IS 'Se le credenziali sono corrette la funzione restituisce true, altrimenti false.';


--
-- TOC entry 221 (class 1255 OID 16779)
-- Name: reset_login(character); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.reset_login(mail character) RETURNS void
    LANGUAGE plpgsql
    AS $$

BEGIN
	UPDATE utente
	SET tentativi_login = 0
	WHERE email = mail;
END;

$$;


ALTER FUNCTION public.reset_login(mail character) OWNER TO postgres;

--
-- TOC entry 218 (class 1255 OID 16735)
-- Name: reset_password(character, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.reset_password(pwd character, mail character varying) RETURNS void
    LANGUAGE plpgsql
    AS $$

BEGIN
	UPDATE utente
	SET password_hash = pwd
	WHERE email = mail;
END;

$$;


ALTER FUNCTION public.reset_password(pwd character, mail character varying) OWNER TO postgres;

--
-- TOC entry 228 (class 1255 OID 16795)
-- Name: ricerca_docenti(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.ricerca_docenti(cod_corso integer) RETURNS TABLE(docente smallint)
    LANGUAGE plpgsql
    AS $$
BEGIN
	RETURN QUERY 
	SELECT tiene.docente
	FROM tiene
	WHERE codice_corso = cod_corso;
END;
$$;


ALTER FUNCTION public.ricerca_docenti(cod_corso integer) OWNER TO postgres;

--
-- TOC entry 2272 (class 0 OID 0)
-- Dependencies: 228
-- Name: FUNCTION ricerca_docenti(cod_corso integer); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.ricerca_docenti(cod_corso integer) IS 'Restituisce i docenti di competenza di un dato corso';


--
-- TOC entry 197 (class 1255 OID 16499)
-- Name: trigger_cartella(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.trigger_cartella() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	DELETE FROM risorsa
	WHERE (tipo = 'cartella' AND percorso IS NOT NULL);
	RETURN NEW;
END;
$$;


ALTER FUNCTION public.trigger_cartella() OWNER TO postgres;

--
-- TOC entry 211 (class 1255 OID 16500)
-- Name: trigger_corsi_studenti(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.trigger_corsi_studenti() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	IF (EXISTS (SELECT *
			   FROM studente JOIN iscritto_a USING (matricola) JOIN corso USING (codice_corso)
			   WHERE studente.corso_laurea != corso.facolta ))
	THEN DELETE FROM iscritto_a
	WHERE codice_corso NOT IN (
		SELECT codice_corso
		FROM studente JOIN iscritto_a USING (matricola) JOIN corso USING (codice_corso)
		WHERE studente.corso_laurea = corso.facolta
	); 			
	END IF;
RETURN NEW;
END;
$$;


ALTER FUNCTION public.trigger_corsi_studenti() OWNER TO postgres;

--
-- TOC entry 213 (class 1255 OID 16729)
-- Name: trigger_sottosezione(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.trigger_sottosezione() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
IF (EXISTS (SELECT codice_sezione
		   FROM sezione
		   WHERE figlio_di IN (
		   	SELECT codice_sezione
		   	FROM sezione
		   	WHERE figlio_di IS NOT NULL)))
	THEN DELETE FROM dezione 
	WHERE NEW.figlio_di  = 
	(SELECT codice_sezione
	FROM sezione
	WHERE figlio_di IS NOT NULL);
END IF;
RETURN NEW;
END;
$$;


ALTER FUNCTION public.trigger_sottosezione() OWNER TO postgres;

--
-- TOC entry 230 (class 1255 OID 16801)
-- Name: verifica_iscrizione_studente(integer, smallint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.verifica_iscrizione_studente(mtrcl integer, cod_corso smallint) RETURNS boolean
    LANGUAGE plpgsql
    AS $$

BEGIN
	IF (EXISTS (SELECT *
		   FROM iscritto_a
		   WHERE matricola = mtrcl AND codice_corso = cod_corso ))
THEN RETURN TRUE;
ELSE RETURN FALSE;

END IF;
END;

$$;


ALTER FUNCTION public.verifica_iscrizione_studente(mtrcl integer, cod_corso smallint) OWNER TO postgres;

--
-- TOC entry 2273 (class 0 OID 0)
-- Dependencies: 230
-- Name: FUNCTION verifica_iscrizione_studente(mtrcl integer, cod_corso smallint); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.verifica_iscrizione_studente(mtrcl integer, cod_corso smallint) IS 'Verifica se uno studente risulta iscritto a un corso';


--
-- TOC entry 231 (class 1255 OID 16803)
-- Name: verifica_iscrizione_studente(integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.verifica_iscrizione_studente(mtrcl integer, cod_corso integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$

BEGIN
	IF (EXISTS (SELECT *
		   FROM iscritto_a
		   WHERE matricola = mtrcl AND codice_corso = cod_corso ))
THEN RETURN TRUE;
ELSE RETURN FALSE;

END IF;
END;

$$;


ALTER FUNCTION public.verifica_iscrizione_studente(mtrcl integer, cod_corso integer) OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 196 (class 1259 OID 16865)
-- Name: accesso_corso; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.accesso_corso (
    inizio_accesso timestamp with time zone NOT NULL,
    fine_accesso timestamp with time zone,
    matricola integer NOT NULL,
    codice_corso smallint NOT NULL
);


ALTER TABLE public.accesso_corso OWNER TO postgres;

--
-- TOC entry 182 (class 1259 OID 16502)
-- Name: amministratore; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.amministratore (
    matricola integer NOT NULL,
    struttura_riferimento character varying(40) NOT NULL
);


ALTER TABLE public.amministratore OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 16505)
-- Name: corso; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.corso (
    codice_corso smallint NOT NULL,
    nome character varying(40) NOT NULL,
    anno_attivazione smallint NOT NULL,
    facolta character varying(40) NOT NULL,
    descrizione character varying(200) NOT NULL,
    peso smallint NOT NULL,
    creatore integer NOT NULL
);


ALTER TABLE public.corso OWNER TO postgres;

--
-- TOC entry 184 (class 1259 OID 16508)
-- Name: docente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.docente (
    matricola integer NOT NULL,
    struttura_riferimento character varying(40) NOT NULL
);


ALTER TABLE public.docente OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 16511)
-- Name: download; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.download (
    data_e_ora timestamp with time zone NOT NULL,
    matricola integer NOT NULL,
    codice_risorsa integer NOT NULL
);


ALTER TABLE public.download OWNER TO postgres;

--
-- TOC entry 186 (class 1259 OID 16514)
-- Name: esame; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.esame (
    codice_corso smallint NOT NULL,
    stato character varying(20) NOT NULL,
    voto character varying(3),
    data date
);


ALTER TABLE public.esame OWNER TO postgres;

--
-- TOC entry 187 (class 1259 OID 16517)
-- Name: facolta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.facolta (
    nome character varying(40) NOT NULL
);


ALTER TABLE public.facolta OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 16520)
-- Name: iscritto_a; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.iscritto_a (
    matricola integer NOT NULL,
    codice_corso smallint NOT NULL
);


ALTER TABLE public.iscritto_a OWNER TO postgres;

--
-- TOC entry 189 (class 1259 OID 16523)
-- Name: risorsa; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.risorsa (
    codice_risorsa integer NOT NULL,
    nome character varying(40) NOT NULL,
    descrizione character varying(200),
    percorso character varying(20) NOT NULL,
    tipo character varying(20) NOT NULL,
    codice_sezione smallint NOT NULL,
    is_pubblica boolean NOT NULL
);


ALTER TABLE public.risorsa OWNER TO postgres;

--
-- TOC entry 190 (class 1259 OID 16526)
-- Name: sessione; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sessione (
    matricola integer NOT NULL,
    inizio_sessione timestamp with time zone NOT NULL,
    fine_sessione timestamp with time zone
);


ALTER TABLE public.sessione OWNER TO postgres;

--
-- TOC entry 191 (class 1259 OID 16529)
-- Name: sezione; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sezione (
    codice_sezione smallint NOT NULL,
    titolo character varying(30) NOT NULL,
    descrizione character varying(200) NOT NULL,
    is_pubblica boolean NOT NULL,
    codice_corso smallint NOT NULL,
    figlio_di smallint,
    matricola integer NOT NULL
);


ALTER TABLE public.sezione OWNER TO postgres;

--
-- TOC entry 192 (class 1259 OID 16532)
-- Name: sostiene; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sostiene (
    matricola integer NOT NULL,
    codice_esame smallint NOT NULL
);


ALTER TABLE public.sostiene OWNER TO postgres;

--
-- TOC entry 193 (class 1259 OID 16535)
-- Name: studente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.studente (
    matricola integer NOT NULL,
    corso_laurea character varying(40) NOT NULL,
    anno_immatricolazione integer NOT NULL,
    stato_carriera character varying(20) NOT NULL
);


ALTER TABLE public.studente OWNER TO postgres;

--
-- TOC entry 195 (class 1259 OID 16762)
-- Name: tiene; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tiene (
    docente smallint NOT NULL,
    codice_corso smallint NOT NULL
);


ALTER TABLE public.tiene OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 16538)
-- Name: utente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.utente (
    matricola integer NOT NULL,
    nome character varying(40) NOT NULL,
    cognome character varying(40) NOT NULL,
    email character varying(50) NOT NULL,
    password_hash character(32) NOT NULL,
    codice_attivazione integer NOT NULL,
    tentativi_login smallint,
    tipo_utente smallint NOT NULL
);


ALTER TABLE public.utente OWNER TO postgres;

--
-- TOC entry 2084 (class 2606 OID 16868)
-- Name: accesso_check; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.accesso_corso
    ADD CONSTRAINT accesso_check CHECK ((inizio_accesso <= fine_accesso)) NOT VALID;


--
-- TOC entry 2116 (class 2606 OID 16872)
-- Name: accesso_corso_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accesso_corso
    ADD CONSTRAINT accesso_corso_pkey PRIMARY KEY (inizio_accesso, matricola, codice_corso);


--
-- TOC entry 2088 (class 2606 OID 16543)
-- Name: amministratore_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.amministratore
    ADD CONSTRAINT amministratore_pkey PRIMARY KEY (matricola);


--
-- TOC entry 2073 (class 2606 OID 16544)
-- Name: corso_anno_attivazione_check; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.corso
    ADD CONSTRAINT corso_anno_attivazione_check CHECK (((anno_attivazione >= 1998) AND ((anno_attivazione)::double precision <= date_part('year'::text, ('now'::text)::date)))) NOT VALID;


--
-- TOC entry 2090 (class 2606 OID 16552)
-- Name: corso_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.corso
    ADD CONSTRAINT corso_pkey PRIMARY KEY (codice_corso);


--
-- TOC entry 2092 (class 2606 OID 16546)
-- Name: docente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.docente
    ADD CONSTRAINT docente_pkey PRIMARY KEY (matricola);


--
-- TOC entry 2074 (class 2606 OID 16547)
-- Name: download_data_e_ora_check; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.download
    ADD CONSTRAINT download_data_e_ora_check CHECK ((data_e_ora <= now())) NOT VALID;


--
-- TOC entry 2094 (class 2606 OID 16549)
-- Name: download_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.download
    ADD CONSTRAINT download_pkey PRIMARY KEY (data_e_ora, matricola, codice_risorsa);


--
-- TOC entry 2075 (class 2606 OID 16550)
-- Name: esame_data_check; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.esame
    ADD CONSTRAINT esame_data_check CHECK ((data <= ('now'::text)::date)) NOT VALID;


--
-- TOC entry 2096 (class 2606 OID 16554)
-- Name: esame_pkey1; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.esame
    ADD CONSTRAINT esame_pkey1 PRIMARY KEY (codice_corso);


--
-- TOC entry 2076 (class 2606 OID 16555)
-- Name: esame_voto_check; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.esame
    ADD CONSTRAINT esame_voto_check CHECK ((((voto)::text = '18'::text) OR ((voto)::text = '19'::text) OR ((voto)::text = '20'::text) OR ((voto)::text = '21'::text) OR ((voto)::text = '22'::text) OR ((voto)::text = '23'::text) OR ((voto)::text = '24'::text) OR ((voto)::text = '25'::text) OR ((voto)::text = '26'::text) OR ((voto)::text = '27'::text) OR ((voto)::text = '28'::text) OR ((voto)::text = '29'::text) OR ((voto)::text = '30'::text) OR ((voto)::text = '30L'::text))) NOT VALID;


--
-- TOC entry 2098 (class 2606 OID 16557)
-- Name: facoltà_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.facolta
    ADD CONSTRAINT "facoltà_pkey" PRIMARY KEY (nome);


--
-- TOC entry 2085 (class 2606 OID 16870)
-- Name: fine_accesso_check; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.accesso_corso
    ADD CONSTRAINT fine_accesso_check CHECK ((fine_accesso <= now())) NOT VALID;


--
-- TOC entry 2086 (class 2606 OID 16869)
-- Name: inizio_accesso_check; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.accesso_corso
    ADD CONSTRAINT inizio_accesso_check CHECK ((inizio_accesso <= now())) NOT VALID;


--
-- TOC entry 2100 (class 2606 OID 16559)
-- Name: iscritto_a_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.iscritto_a
    ADD CONSTRAINT iscritto_a_pkey PRIMARY KEY (matricola, codice_corso);


--
-- TOC entry 2102 (class 2606 OID 16561)
-- Name: risorsa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.risorsa
    ADD CONSTRAINT risorsa_pkey PRIMARY KEY (codice_risorsa);


--
-- TOC entry 2077 (class 2606 OID 16562)
-- Name: sessione_check; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.sessione
    ADD CONSTRAINT sessione_check CHECK ((inizio_sessione <= fine_sessione)) NOT VALID;


--
-- TOC entry 2078 (class 2606 OID 16563)
-- Name: sessione_fine_sessione_check; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.sessione
    ADD CONSTRAINT sessione_fine_sessione_check CHECK ((fine_sessione <= now())) NOT VALID;


--
-- TOC entry 2079 (class 2606 OID 16564)
-- Name: sessione_inizio_sessione_check; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.sessione
    ADD CONSTRAINT sessione_inizio_sessione_check CHECK ((inizio_sessione <= now())) NOT VALID;


--
-- TOC entry 2104 (class 2606 OID 16566)
-- Name: sessione_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sessione
    ADD CONSTRAINT sessione_pkey PRIMARY KEY (inizio_sessione, matricola);


--
-- TOC entry 2106 (class 2606 OID 16568)
-- Name: sezione_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sezione
    ADD CONSTRAINT sezione_pkey PRIMARY KEY (codice_sezione);


--
-- TOC entry 2108 (class 2606 OID 16570)
-- Name: sostiene_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sostiene
    ADD CONSTRAINT sostiene_pkey PRIMARY KEY (matricola, codice_esame);


--
-- TOC entry 2080 (class 2606 OID 16849)
-- Name: studente_anno_immatricolazione_check; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.studente
    ADD CONSTRAINT studente_anno_immatricolazione_check CHECK (((anno_immatricolazione >= 1998) AND ((anno_immatricolazione)::double precision <= date_part('year'::text, ('now'::text)::date)))) NOT VALID;


--
-- TOC entry 2110 (class 2606 OID 16573)
-- Name: studente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.studente
    ADD CONSTRAINT studente_pkey PRIMARY KEY (matricola);


--
-- TOC entry 2114 (class 2606 OID 16766)
-- Name: tiene_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tiene
    ADD CONSTRAINT tiene_pkey PRIMARY KEY (docente, codice_corso);


--
-- TOC entry 2081 (class 2606 OID 16574)
-- Name: utente_matricola_check; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.utente
    ADD CONSTRAINT utente_matricola_check CHECK ((matricola >= 0)) NOT VALID;


--
-- TOC entry 2112 (class 2606 OID 16576)
-- Name: utente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (matricola);


--
-- TOC entry 2082 (class 2606 OID 16577)
-- Name: utente_tentativi_login_check; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.utente
    ADD CONSTRAINT utente_tentativi_login_check CHECK (((tentativi_login >= 0) AND (tentativi_login <= 10))) NOT VALID;


--
-- TOC entry 2083 (class 2606 OID 16578)
-- Name: utente_tipo_utente_check; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.utente
    ADD CONSTRAINT utente_tipo_utente_check CHECK (((tipo_utente >= 1) AND (tipo_utente <= 3))) NOT VALID;


--
-- TOC entry 2139 (class 2620 OID 16750)
-- Name: corsi_compatibili_con_la_facolta; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER corsi_compatibili_con_la_facolta AFTER INSERT OR UPDATE OF matricola, codice_corso ON public.iscritto_a FOR EACH ROW EXECUTE PROCEDURE public.trigger_corsi_studenti();


--
-- TOC entry 2141 (class 2620 OID 16748)
-- Name: nessuna_sotto_sottosezione; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER nessuna_sotto_sottosezione AFTER INSERT OR UPDATE OF figlio_di ON public.sezione FOR EACH ROW EXECUTE PROCEDURE public.trigger_sottosezione();


--
-- TOC entry 2140 (class 2620 OID 16749)
-- Name: nessuna_sottocartella; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER nessuna_sottocartella AFTER INSERT OR UPDATE ON public.risorsa FOR EACH ROW WHEN ((((new.tipo)::text = 'cartella'::text) AND (new.percorso IS NOT NULL))) EXECUTE PROCEDURE public.trigger_cartella();


--
-- TOC entry 2137 (class 2606 OID 16873)
-- Name: accesso_corso_codice_corso_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accesso_corso
    ADD CONSTRAINT accesso_corso_codice_corso_fkey FOREIGN KEY (codice_corso) REFERENCES public.corso(codice_corso) ON UPDATE CASCADE;


--
-- TOC entry 2138 (class 2606 OID 16878)
-- Name: accesso_corso_matricola_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accesso_corso
    ADD CONSTRAINT accesso_corso_matricola_fkey FOREIGN KEY (matricola) REFERENCES public.utente(matricola) ON UPDATE CASCADE;


--
-- TOC entry 2117 (class 2606 OID 16582)
-- Name: amministratore_matricola_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.amministratore
    ADD CONSTRAINT amministratore_matricola_fkey FOREIGN KEY (matricola) REFERENCES public.utente(matricola) ON UPDATE CASCADE;


--
-- TOC entry 2118 (class 2606 OID 16587)
-- Name: corso_creatore_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.corso
    ADD CONSTRAINT corso_creatore_fkey FOREIGN KEY (creatore) REFERENCES public.amministratore(matricola) ON UPDATE CASCADE;


--
-- TOC entry 2119 (class 2606 OID 16592)
-- Name: corso_facoltà_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.corso
    ADD CONSTRAINT "corso_facoltà_fkey" FOREIGN KEY (facolta) REFERENCES public.facolta(nome) ON UPDATE CASCADE;


--
-- TOC entry 2136 (class 2606 OID 16772)
-- Name: corso_tenuto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tiene
    ADD CONSTRAINT corso_tenuto_fkey FOREIGN KEY (codice_corso) REFERENCES public.corso(codice_corso) ON UPDATE CASCADE;


--
-- TOC entry 2135 (class 2606 OID 16767)
-- Name: docente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tiene
    ADD CONSTRAINT docente_fkey FOREIGN KEY (docente) REFERENCES public.docente(matricola) ON UPDATE CASCADE;


--
-- TOC entry 2120 (class 2606 OID 16597)
-- Name: docente_matricola_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.docente
    ADD CONSTRAINT docente_matricola_fkey FOREIGN KEY (matricola) REFERENCES public.utente(matricola) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2121 (class 2606 OID 16602)
-- Name: download_codice_risorsa_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.download
    ADD CONSTRAINT download_codice_risorsa_fkey FOREIGN KEY (codice_risorsa) REFERENCES public.risorsa(codice_risorsa) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2122 (class 2606 OID 16607)
-- Name: download_matricola_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.download
    ADD CONSTRAINT download_matricola_fkey FOREIGN KEY (matricola) REFERENCES public.utente(matricola) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2123 (class 2606 OID 16612)
-- Name: esame_codice_corso_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.esame
    ADD CONSTRAINT esame_codice_corso_fkey FOREIGN KEY (codice_corso) REFERENCES public.corso(codice_corso) ON UPDATE CASCADE;


--
-- TOC entry 2124 (class 2606 OID 16617)
-- Name: iscritto_a_codice_corso_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.iscritto_a
    ADD CONSTRAINT iscritto_a_codice_corso_fkey FOREIGN KEY (codice_corso) REFERENCES public.corso(codice_corso) ON UPDATE CASCADE;


--
-- TOC entry 2125 (class 2606 OID 16622)
-- Name: iscritto_a_matricola_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.iscritto_a
    ADD CONSTRAINT iscritto_a_matricola_fkey FOREIGN KEY (matricola) REFERENCES public.studente(matricola) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2126 (class 2606 OID 16627)
-- Name: risorsa_codice_sezione_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.risorsa
    ADD CONSTRAINT risorsa_codice_sezione_fkey FOREIGN KEY (codice_sezione) REFERENCES public.sezione(codice_sezione) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2127 (class 2606 OID 16632)
-- Name: sessione_matricola_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sessione
    ADD CONSTRAINT sessione_matricola_fkey FOREIGN KEY (matricola) REFERENCES public.utente(matricola) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2130 (class 2606 OID 16637)
-- Name: sezione_codice_corso_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sezione
    ADD CONSTRAINT sezione_codice_corso_fkey FOREIGN KEY (codice_corso) REFERENCES public.corso(codice_corso) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2129 (class 2606 OID 16642)
-- Name: sezione_figlio_di_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sezione
    ADD CONSTRAINT sezione_figlio_di_fkey FOREIGN KEY (figlio_di) REFERENCES public.sezione(codice_sezione) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2128 (class 2606 OID 16647)
-- Name: sezione_matricola_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sezione
    ADD CONSTRAINT sezione_matricola_fkey FOREIGN KEY (matricola) REFERENCES public.docente(matricola) ON UPDATE CASCADE;


--
-- TOC entry 2131 (class 2606 OID 16652)
-- Name: sostiene_codice_esame_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sostiene
    ADD CONSTRAINT sostiene_codice_esame_fkey FOREIGN KEY (codice_esame) REFERENCES public.esame(codice_corso);


--
-- TOC entry 2132 (class 2606 OID 16657)
-- Name: sostiene_matricola_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sostiene
    ADD CONSTRAINT sostiene_matricola_fkey FOREIGN KEY (matricola) REFERENCES public.studente(matricola) ON UPDATE CASCADE;


--
-- TOC entry 2133 (class 2606 OID 16662)
-- Name: studente_corso_laurea_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.studente
    ADD CONSTRAINT studente_corso_laurea_fkey FOREIGN KEY (corso_laurea) REFERENCES public.facolta(nome) ON UPDATE CASCADE;


--
-- TOC entry 2134 (class 2606 OID 16667)
-- Name: studente_matricola_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.studente
    ADD CONSTRAINT studente_matricola_fkey FOREIGN KEY (matricola) REFERENCES public.utente(matricola) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2263 (class 0 OID 0)
-- Dependencies: 7
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2019-01-22 10:09:05

--
-- PostgreSQL database dump complete
--

