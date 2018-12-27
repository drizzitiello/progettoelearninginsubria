--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.15
-- Dumped by pg_dump version 9.5.15

-- Started on 2018-12-27 18:25:51

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2233 (class 0 OID 16502)
-- Dependencies: 182
-- Data for Name: amministratore; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.amministratore (matricola, struttura_riferimento) FROM stdin;
2	Informatica
\.


--
-- TOC entry 2234 (class 0 OID 16505)
-- Dependencies: 183
-- Data for Name: corso; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.corso (codice_corso, nome, anno_attivazione, facolta, descrizione, peso, creatore) FROM stdin;
1999	Prog	2000	Informatica	Corso per imparare a programmare con quella cagata di prog.io	9	2
3000	Useless	2013	Informatica	Corso inutile	1	2
4343	ffdfd	2000	merendine	ee	1	2
89	botanica	2015	Informatica	pannocchie	15	2
80	storia del cinema	1998	merendine	guardiamo film	10	2
77	programmazione tentacolare	2010	Informatica	programmazione per cefalopodi	6	2
9	pesce ghiacciato	2013	merendine	saa	6	2
99	pellegrinaggio	2000	Informatica	sasa	0	2
\.


--
-- TOC entry 2235 (class 0 OID 16508)
-- Dependencies: 184
-- Data for Name: docente; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.docente (matricola, struttura_riferimento) FROM stdin;
13	Informatica
\.


--
-- TOC entry 2236 (class 0 OID 16511)
-- Dependencies: 185
-- Data for Name: download; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.download (data_e_ora, matricola, codice_risorsa) FROM stdin;
2018-11-27 00:00:00+01	2	45
\.


--
-- TOC entry 2237 (class 0 OID 16514)
-- Dependencies: 186
-- Data for Name: esame; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.esame (codice_corso, stato, voto, data) FROM stdin;
1999	Passato	25	2010-12-22
\.


--
-- TOC entry 2238 (class 0 OID 16517)
-- Dependencies: 187
-- Data for Name: facolta; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.facolta (nome) FROM stdin;
Informatica
merendine
\.


--
-- TOC entry 2239 (class 0 OID 16520)
-- Dependencies: 188
-- Data for Name: iscritto_a; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.iscritto_a (matricola, codice_corso) FROM stdin;
34	1999
34	3000
1	1999
1	3000
1	89
1	77
\.


--
-- TOC entry 2240 (class 0 OID 16523)
-- Dependencies: 189
-- Data for Name: risorsa; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.risorsa (codice_risorsa, nome, descrizione, percorso, tipo, codice_sezione, is_pubblica) FROM stdin;
22	ris1	prima risorsa	w	r	3	t
45	ris2	seconda risorsa	C://h	word	5	t
\.


--
-- TOC entry 2241 (class 0 OID 16526)
-- Dependencies: 190
-- Data for Name: sessione; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sessione (matricola, inizio_sessione, fine_sessione) FROM stdin;
34	2018-11-28 10:00:00+01	2018-11-28 10:30:00+01
\.


--
-- TOC entry 2242 (class 0 OID 16529)
-- Dependencies: 191
-- Data for Name: sezione; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sezione (codice_sezione, titolo, descrizione, is_pubblica, codice_corso, figlio_di, matricola) FROM stdin;
3	sez1	prima sez	t	1999	\N	13
5	sez2	seconda sez	t	1999	3	13
\.


--
-- TOC entry 2243 (class 0 OID 16532)
-- Dependencies: 192
-- Data for Name: sostiene; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sostiene (matricola, codice_esame) FROM stdin;
34	1999
\.


--
-- TOC entry 2244 (class 0 OID 16535)
-- Dependencies: 193
-- Data for Name: studente; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.studente (matricola, corso_laurea, anno_immatricolazione, stato_carriera) FROM stdin;
34	Informatica	2018	1 anno
1	Informatica	2017	2 anno
\.


--
-- TOC entry 2246 (class 0 OID 16762)
-- Dependencies: 195
-- Data for Name: tiene; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tiene (docente, codice_corso) FROM stdin;
13	99
\.


--
-- TOC entry 2245 (class 0 OID 16538)
-- Dependencies: 194
-- Data for Name: utente; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.utente (matricola, nome, cognome, email, password_hash, codice_attivazione, tentativi_login, tipo_utente) FROM stdin;
1	Ciccio	Pasticcio	ciao	6E6BC4E49DD477EBC98EF4046C067B5F	3434	\N	1
2	Adriano	Galliani	agag	6E6BC4E49DD477EBC98EF4046C067B5F	2020	0	3
34	pippo	pluto	aaaa	6E6BC4E49DD477EBC98EF4046C067B5F	45	0	1
33	Cacca	Bomba	sasa	C21A7F50739500292AB24DD37150FA8A	33	1	1
13	Aria	Stark	aaa	6E6BC4E49DD477EBC98EF4046C067B5F	11	0	2
\.


-- Completed on 2018-12-27 18:25:52

--
-- PostgreSQL database dump complete
--

