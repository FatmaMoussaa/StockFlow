--
-- PostgreSQL database dump
--

\restrict MsZgXnWkiGU5XwniMCcLG9AwdNtlpU6CPOgkGv2G88tekPPd4Mgy70VTZbEOBmc

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.1

-- Started on 2026-11-16 15:51:50

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 890 (class 1247 OID 18924)
-- Name: etatarticle; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.etatarticle AS ENUM (
    'BON',
    'DEFECTUEUX',
    'PARTIEL'
);


ALTER TYPE public.etatarticle OWNER TO postgres;

--
-- TOC entry 884 (class 1247 OID 18904)
-- Name: statutcommande; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.statutcommande AS ENUM (
    'EN_ATTENTE',
    'VALIDEE',
    'REJETEE',
    'RETOURNEE'
);


ALTER TYPE public.statutcommande OWNER TO postgres;

--
-- TOC entry 893 (class 1247 OID 18932)
-- Name: statutinventaire; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.statutinventaire AS ENUM (
    'EN_COURS',
    'TERMINE',
    'VALIDE'
);


ALTER TYPE public.statutinventaire OWNER TO postgres;

--
-- TOC entry 881 (class 1247 OID 18879)
-- Name: typearticle; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.typearticle AS ENUM (
    'CONSOMMABLE_BUREAUTIQUE',
    'CONSOMMABLE_INFORMATIQUE',
    'CONSOMMABLE_TECHNOLOGIE',
    'CONSOMMABLE_TIRAGE',
    'CONSOMMABLE_NETTOYAGE',
    'CONSOMMABLE_ENTRETIEN',
    'CONSOMMABLE_JARDIN',
    'CONSOMMABLE_DIVERS',
    'DURABLE_MEUBLE',
    'DURABLE_INFORMATIQUE',
    'DURABLE_TECHNOLOGIE',
    'DURABLE_DIVERS'
);


ALTER TYPE public.typearticle OWNER TO postgres;

--
-- TOC entry 938 (class 1247 OID 19323)
-- Name: typelocal; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.typelocal AS ENUM (
    'Amphi',
    'Salle',
    'Administration',
    'Bibliotheque',
    'Magasin',
    'Bureau_enseignant',
    'Consommation_immediate'
);


ALTER TYPE public.typelocal OWNER TO postgres;

--
-- TOC entry 887 (class 1247 OID 18914)
-- Name: typemouvement; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.typemouvement AS ENUM (
    'ENTREE',
    'SORTIE',
    'AJUSTEMENT',
    'RETOUR',
    'AUTRE'
);


ALTER TYPE public.typemouvement OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 220 (class 1259 OID 18940)
-- Name: article; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.article (
    idarticle integer NOT NULL,
    reference character varying(50),
    codebarres character varying(50),
    designation character varying(100),
    description text,
    type character varying(50),
    dateperemption date,
    stockminimal integer,
    estcritique boolean,
    idlocal integer
);


ALTER TABLE public.article OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 18939)
-- Name: article_idarticle_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.article_idarticle_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.article_idarticle_seq OWNER TO postgres;

--
-- TOC entry 5176 (class 0 OID 0)
-- Dependencies: 219
-- Name: article_idarticle_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.article_idarticle_seq OWNED BY public.article.idarticle;


--
-- TOC entry 230 (class 1259 OID 18976)
-- Name: commandeexterne; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.commandeexterne (
    idcommande integer NOT NULL,
    datecommande date,
    datereception date,
    statut character varying(20) DEFAULT 'EN_ATTENTE'::character varying,
    montant double precision,
    idfournisseur integer
);


ALTER TABLE public.commandeexterne OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 18975)
-- Name: commandeexterne_idcommande_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.commandeexterne_idcommande_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.commandeexterne_idcommande_seq OWNER TO postgres;

--
-- TOC entry 5177 (class 0 OID 0)
-- Dependencies: 229
-- Name: commandeexterne_idcommande_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.commandeexterne_idcommande_seq OWNED BY public.commandeexterne.idcommande;


--
-- TOC entry 232 (class 1259 OID 18982)
-- Name: commandeinterne; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.commandeinterne (
    idcommande integer NOT NULL,
    datecommande date,
    statut character varying(20),
    idservice integer
);


ALTER TABLE public.commandeinterne OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 18981)
-- Name: commandeinterne_idcommande_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.commandeinterne_idcommande_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.commandeinterne_idcommande_seq OWNER TO postgres;

--
-- TOC entry 5178 (class 0 OID 0)
-- Dependencies: 231
-- Name: commandeinterne_idcommande_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.commandeinterne_idcommande_seq OWNED BY public.commandeinterne.idcommande;


--
-- TOC entry 240 (class 1259 OID 19022)
-- Name: controle; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.controle (
    idcontrole integer NOT NULL,
    datecontrole date,
    quantitecontrollee integer,
    etat character varying(20),
    observation text,
    idarticle integer,
    idlocalsource integer,
    idlocaldestination integer,
    idcommandeinterne integer,
    idcommandeexterne integer
);


ALTER TABLE public.controle OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 19021)
-- Name: controle_idcontrole_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.controle_idcontrole_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.controle_idcontrole_seq OWNER TO postgres;

--
-- TOC entry 5179 (class 0 OID 0)
-- Dependencies: 239
-- Name: controle_idcontrole_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.controle_idcontrole_seq OWNED BY public.controle.idcontrole;


--
-- TOC entry 226 (class 1259 OID 18962)
-- Name: fournisseur; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.fournisseur (
    idfournisseur integer NOT NULL,
    nomfournisseur character varying(100),
    adresse text,
    telephone character varying(20),
    email character varying(100),
    contactprincipal character varying(100)
);


ALTER TABLE public.fournisseur OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 18961)
-- Name: fournisseur_idfournisseur_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.fournisseur_idfournisseur_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.fournisseur_idfournisseur_seq OWNER TO postgres;

--
-- TOC entry 5180 (class 0 OID 0)
-- Dependencies: 225
-- Name: fournisseur_idfournisseur_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.fournisseur_idfournisseur_seq OWNED BY public.fournisseur.idfournisseur;


--
-- TOC entry 242 (class 1259 OID 19030)
-- Name: inventaire; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.inventaire (
    idinventaire integer NOT NULL,
    dateinventaire date,
    statut character varying(20),
    notes text
);


ALTER TABLE public.inventaire OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 19029)
-- Name: inventaire_idinventaire_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.inventaire_idinventaire_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.inventaire_idinventaire_seq OWNER TO postgres;

--
-- TOC entry 5181 (class 0 OID 0)
-- Dependencies: 241
-- Name: inventaire_idinventaire_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.inventaire_idinventaire_seq OWNED BY public.inventaire.idinventaire;


--
-- TOC entry 234 (class 1259 OID 18988)
-- Name: lignecommande; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.lignecommande (
    idligne integer NOT NULL,
    idcommandeexterne integer,
    idcommandeinterne integer,
    idarticle integer,
    quantite integer,
    prixunitaire double precision,
    soustotal double precision,
    idlocal integer
);


ALTER TABLE public.lignecommande OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 18987)
-- Name: lignecommande_idligne_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.lignecommande_idligne_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.lignecommande_idligne_seq OWNER TO postgres;

--
-- TOC entry 5182 (class 0 OID 0)
-- Dependencies: 233
-- Name: lignecommande_idligne_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.lignecommande_idligne_seq OWNED BY public.lignecommande.idligne;


--
-- TOC entry 244 (class 1259 OID 19235)
-- Name: lignecommandeinterne; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.lignecommandeinterne (
    idligne integer NOT NULL,
    idcommandeinterne integer NOT NULL,
    idarticle integer NOT NULL,
    quantite integer NOT NULL,
    idlocalsource integer,
    idlocaldestination integer,
    retoureffectue boolean DEFAULT false,
    CONSTRAINT lignecommandeinterne_quantite_check CHECK ((quantite > 0))
);


ALTER TABLE public.lignecommandeinterne OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 19234)
-- Name: lignecommandeinterne_idligne_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.lignecommandeinterne_idligne_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.lignecommandeinterne_idligne_seq OWNER TO postgres;

--
-- TOC entry 5183 (class 0 OID 0)
-- Dependencies: 243
-- Name: lignecommandeinterne_idligne_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.lignecommandeinterne_idligne_seq OWNED BY public.lignecommandeinterne.idligne;


--
-- TOC entry 246 (class 1259 OID 19269)
-- Name: ligneinventaire; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ligneinventaire (
    idligne integer NOT NULL,
    idinventaire integer NOT NULL,
    idarticle integer NOT NULL,
    idlocal integer NOT NULL,
    quantite integer NOT NULL,
    CONSTRAINT ligneinventaire_quantite_check CHECK ((quantite >= 0))
);


ALTER TABLE public.ligneinventaire OWNER TO postgres;

--
-- TOC entry 245 (class 1259 OID 19268)
-- Name: ligneinventaire_idligne_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ligneinventaire_idligne_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ligneinventaire_idligne_seq OWNER TO postgres;

--
-- TOC entry 5184 (class 0 OID 0)
-- Dependencies: 245
-- Name: ligneinventaire_idligne_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ligneinventaire_idligne_seq OWNED BY public.ligneinventaire.idligne;


--
-- TOC entry 222 (class 1259 OID 18948)
-- Name: local; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.local (
    idlocal integer NOT NULL,
    nomlocal character varying(50),
    capacite integer,
    adresse text,
    typelocal public.typelocal
);


ALTER TABLE public.local OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 18947)
-- Name: local_idlocal_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.local_idlocal_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.local_idlocal_seq OWNER TO postgres;

--
-- TOC entry 5185 (class 0 OID 0)
-- Dependencies: 221
-- Name: local_idlocal_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.local_idlocal_seq OWNED BY public.local.idlocal;


--
-- TOC entry 238 (class 1259 OID 19014)
-- Name: mouvement; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.mouvement (
    idmouvement integer NOT NULL,
    typemouvement character varying(20),
    idarticle integer,
    quantite integer,
    datemouvement timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    observation text,
    idlocal integer,
    idcommandeinterne integer,
    idcommandeexterne integer
);


ALTER TABLE public.mouvement OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 19013)
-- Name: mouvement_idmouvement_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.mouvement_idmouvement_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.mouvement_idmouvement_seq OWNER TO postgres;

--
-- TOC entry 5186 (class 0 OID 0)
-- Dependencies: 237
-- Name: mouvement_idmouvement_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.mouvement_idmouvement_seq OWNED BY public.mouvement.idmouvement;


--
-- TOC entry 228 (class 1259 OID 18970)
-- Name: service; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.service (
    idservice integer NOT NULL,
    nomservice character varying(100),
    responsable character varying(100),
    telephone character varying(20),
    email character varying(100),
    typeservice character varying(70)
);


ALTER TABLE public.service OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 18969)
-- Name: service_idservice_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.service_idservice_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.service_idservice_seq OWNER TO postgres;

--
-- TOC entry 5187 (class 0 OID 0)
-- Dependencies: 227
-- Name: service_idservice_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.service_idservice_seq OWNED BY public.service.idservice;


--
-- TOC entry 224 (class 1259 OID 18956)
-- Name: stock; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.stock (
    idstock integer NOT NULL,
    idarticle integer,
    idlocal integer,
    quantiteactuelle integer,
    quantitereservee integer,
    datemaj date
);


ALTER TABLE public.stock OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 18955)
-- Name: stock_idstock_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.stock_idstock_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.stock_idstock_seq OWNER TO postgres;

--
-- TOC entry 5188 (class 0 OID 0)
-- Dependencies: 223
-- Name: stock_idstock_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.stock_idstock_seq OWNED BY public.stock.idstock;


--
-- TOC entry 236 (class 1259 OID 19006)
-- Name: trace; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trace (
    idtrace integer NOT NULL,
    dateoperation timestamp without time zone,
    typeoperation character varying(50),
    utilisateur character varying(50),
    details text
);


ALTER TABLE public.trace OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 19005)
-- Name: trace_idtrace_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trace_idtrace_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.trace_idtrace_seq OWNER TO postgres;

--
-- TOC entry 5189 (class 0 OID 0)
-- Dependencies: 235
-- Name: trace_idtrace_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trace_idtrace_seq OWNED BY public.trace.idtrace;


--
-- TOC entry 248 (class 1259 OID 20596)
-- Name: utilisateurs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.utilisateurs (
    id_utilisateur integer NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(255) NOT NULL,
    role character varying(20) DEFAULT 'USER'::character varying,
    nom_complet character varying(100),
    email character varying(100),
    date_creation timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    dernier_login timestamp without time zone,
    actif boolean DEFAULT true
);


ALTER TABLE public.utilisateurs OWNER TO postgres;

--
-- TOC entry 247 (class 1259 OID 20595)
-- Name: utilisateurs_id_utilisateur_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.utilisateurs_id_utilisateur_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.utilisateurs_id_utilisateur_seq OWNER TO postgres;

--
-- TOC entry 5190 (class 0 OID 0)
-- Dependencies: 247
-- Name: utilisateurs_id_utilisateur_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.utilisateurs_id_utilisateur_seq OWNED BY public.utilisateurs.id_utilisateur;


--
-- TOC entry 4944 (class 2604 OID 18943)
-- Name: article idarticle; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.article ALTER COLUMN idarticle SET DEFAULT nextval('public.article_idarticle_seq'::regclass);


--
-- TOC entry 4949 (class 2604 OID 18979)
-- Name: commandeexterne idcommande; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commandeexterne ALTER COLUMN idcommande SET DEFAULT nextval('public.commandeexterne_idcommande_seq'::regclass);


--
-- TOC entry 4951 (class 2604 OID 18985)
-- Name: commandeinterne idcommande; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commandeinterne ALTER COLUMN idcommande SET DEFAULT nextval('public.commandeinterne_idcommande_seq'::regclass);


--
-- TOC entry 4956 (class 2604 OID 19025)
-- Name: controle idcontrole; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.controle ALTER COLUMN idcontrole SET DEFAULT nextval('public.controle_idcontrole_seq'::regclass);


--
-- TOC entry 4947 (class 2604 OID 18965)
-- Name: fournisseur idfournisseur; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.fournisseur ALTER COLUMN idfournisseur SET DEFAULT nextval('public.fournisseur_idfournisseur_seq'::regclass);


--
-- TOC entry 4957 (class 2604 OID 19033)
-- Name: inventaire idinventaire; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventaire ALTER COLUMN idinventaire SET DEFAULT nextval('public.inventaire_idinventaire_seq'::regclass);


--
-- TOC entry 4952 (class 2604 OID 18991)
-- Name: lignecommande idligne; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lignecommande ALTER COLUMN idligne SET DEFAULT nextval('public.lignecommande_idligne_seq'::regclass);


--
-- TOC entry 4958 (class 2604 OID 19238)
-- Name: lignecommandeinterne idligne; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lignecommandeinterne ALTER COLUMN idligne SET DEFAULT nextval('public.lignecommandeinterne_idligne_seq'::regclass);


--
-- TOC entry 4960 (class 2604 OID 19272)
-- Name: ligneinventaire idligne; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ligneinventaire ALTER COLUMN idligne SET DEFAULT nextval('public.ligneinventaire_idligne_seq'::regclass);


--
-- TOC entry 4945 (class 2604 OID 18951)
-- Name: local idlocal; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.local ALTER COLUMN idlocal SET DEFAULT nextval('public.local_idlocal_seq'::regclass);


--
-- TOC entry 4954 (class 2604 OID 19017)
-- Name: mouvement idmouvement; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mouvement ALTER COLUMN idmouvement SET DEFAULT nextval('public.mouvement_idmouvement_seq'::regclass);


--
-- TOC entry 4948 (class 2604 OID 18973)
-- Name: service idservice; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service ALTER COLUMN idservice SET DEFAULT nextval('public.service_idservice_seq'::regclass);


--
-- TOC entry 4946 (class 2604 OID 18959)
-- Name: stock idstock; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock ALTER COLUMN idstock SET DEFAULT nextval('public.stock_idstock_seq'::regclass);


--
-- TOC entry 4953 (class 2604 OID 19009)
-- Name: trace idtrace; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trace ALTER COLUMN idtrace SET DEFAULT nextval('public.trace_idtrace_seq'::regclass);


--
-- TOC entry 4961 (class 2604 OID 20599)
-- Name: utilisateurs id_utilisateur; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilisateurs ALTER COLUMN id_utilisateur SET DEFAULT nextval('public.utilisateurs_id_utilisateur_seq'::regclass);


--
-- TOC entry 4968 (class 2606 OID 19044)
-- Name: article article_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.article
    ADD CONSTRAINT article_pkey PRIMARY KEY (idarticle);


--
-- TOC entry 4978 (class 2606 OID 19054)
-- Name: commandeexterne commandeexterne_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commandeexterne
    ADD CONSTRAINT commandeexterne_pkey PRIMARY KEY (idcommande);


--
-- TOC entry 4980 (class 2606 OID 19056)
-- Name: commandeinterne commandeinterne_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commandeinterne
    ADD CONSTRAINT commandeinterne_pkey PRIMARY KEY (idcommande);


--
-- TOC entry 4988 (class 2606 OID 19068)
-- Name: controle controle_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.controle
    ADD CONSTRAINT controle_pkey PRIMARY KEY (idcontrole);


--
-- TOC entry 4974 (class 2606 OID 19050)
-- Name: fournisseur fournisseur_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.fournisseur
    ADD CONSTRAINT fournisseur_pkey PRIMARY KEY (idfournisseur);


--
-- TOC entry 4990 (class 2606 OID 19070)
-- Name: inventaire inventaire_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventaire
    ADD CONSTRAINT inventaire_pkey PRIMARY KEY (idinventaire);


--
-- TOC entry 4982 (class 2606 OID 19058)
-- Name: lignecommande lignecommande_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lignecommande
    ADD CONSTRAINT lignecommande_pkey PRIMARY KEY (idligne);


--
-- TOC entry 4992 (class 2606 OID 19246)
-- Name: lignecommandeinterne lignecommandeinterne_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lignecommandeinterne
    ADD CONSTRAINT lignecommandeinterne_pkey PRIMARY KEY (idligne);


--
-- TOC entry 4994 (class 2606 OID 19280)
-- Name: ligneinventaire ligneinventaire_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ligneinventaire
    ADD CONSTRAINT ligneinventaire_pkey PRIMARY KEY (idligne);


--
-- TOC entry 4970 (class 2606 OID 19046)
-- Name: local local_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.local
    ADD CONSTRAINT local_pkey PRIMARY KEY (idlocal);


--
-- TOC entry 4986 (class 2606 OID 19066)
-- Name: mouvement mouvement_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mouvement
    ADD CONSTRAINT mouvement_pkey PRIMARY KEY (idmouvement);


--
-- TOC entry 4976 (class 2606 OID 19052)
-- Name: service service_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service
    ADD CONSTRAINT service_pkey PRIMARY KEY (idservice);


--
-- TOC entry 4972 (class 2606 OID 19048)
-- Name: stock stock_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock
    ADD CONSTRAINT stock_pkey PRIMARY KEY (idstock);


--
-- TOC entry 4984 (class 2606 OID 19064)
-- Name: trace trace_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trace
    ADD CONSTRAINT trace_pkey PRIMARY KEY (idtrace);


--
-- TOC entry 4998 (class 2606 OID 20609)
-- Name: utilisateurs utilisateurs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilisateurs
    ADD CONSTRAINT utilisateurs_pkey PRIMARY KEY (id_utilisateur);


--
-- TOC entry 5000 (class 2606 OID 20611)
-- Name: utilisateurs utilisateurs_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilisateurs
    ADD CONSTRAINT utilisateurs_username_key UNIQUE (username);


--
-- TOC entry 4995 (class 1259 OID 20613)
-- Name: idx_utilisateurs_role; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_utilisateurs_role ON public.utilisateurs USING btree (role);


--
-- TOC entry 4996 (class 1259 OID 20612)
-- Name: idx_utilisateurs_username; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_utilisateurs_username ON public.utilisateurs USING btree (username);


--
-- TOC entry 5001 (class 2606 OID 20071)
-- Name: article fk_article_local; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.article
    ADD CONSTRAINT fk_article_local FOREIGN KEY (idlocal) REFERENCES public.local(idlocal) ON DELETE CASCADE;


--
-- TOC entry 5004 (class 2606 OID 19189)
-- Name: commandeexterne fk_cmdext_fournisseur; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commandeexterne
    ADD CONSTRAINT fk_cmdext_fournisseur FOREIGN KEY (idfournisseur) REFERENCES public.fournisseur(idfournisseur) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5005 (class 2606 OID 19194)
-- Name: commandeinterne fk_cmdint_service; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commandeinterne
    ADD CONSTRAINT fk_cmdint_service FOREIGN KEY (idservice) REFERENCES public.service(idservice) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5014 (class 2606 OID 19229)
-- Name: controle fk_controle_article; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.controle
    ADD CONSTRAINT fk_controle_article FOREIGN KEY (idarticle) REFERENCES public.article(idarticle) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5015 (class 2606 OID 19840)
-- Name: controle fk_controle_commande_externe; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.controle
    ADD CONSTRAINT fk_controle_commande_externe FOREIGN KEY (idcommandeexterne) REFERENCES public.commandeexterne(idcommande);


--
-- TOC entry 5016 (class 2606 OID 19835)
-- Name: controle fk_controle_commande_interne; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.controle
    ADD CONSTRAINT fk_controle_commande_interne FOREIGN KEY (idcommandeinterne) REFERENCES public.commandeinterne(idcommande);


--
-- TOC entry 5006 (class 2606 OID 19199)
-- Name: lignecommande fk_ligne_article; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lignecommande
    ADD CONSTRAINT fk_ligne_article FOREIGN KEY (idarticle) REFERENCES public.article(idarticle) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5007 (class 2606 OID 19204)
-- Name: lignecommande fk_ligne_cmdexterne; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lignecommande
    ADD CONSTRAINT fk_ligne_cmdexterne FOREIGN KEY (idcommandeexterne) REFERENCES public.commandeexterne(idcommande) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5008 (class 2606 OID 19209)
-- Name: lignecommande fk_ligne_cmdinterne; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lignecommande
    ADD CONSTRAINT fk_ligne_cmdinterne FOREIGN KEY (idcommandeinterne) REFERENCES public.commandeinterne(idcommande) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5009 (class 2606 OID 19214)
-- Name: lignecommande fk_ligne_local; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lignecommande
    ADD CONSTRAINT fk_ligne_local FOREIGN KEY (idlocal) REFERENCES public.local(idlocal) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5017 (class 2606 OID 19855)
-- Name: lignecommandeinterne fk_ligne_localdestination; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lignecommandeinterne
    ADD CONSTRAINT fk_ligne_localdestination FOREIGN KEY (idlocaldestination) REFERENCES public.local(idlocal);


--
-- TOC entry 5018 (class 2606 OID 19850)
-- Name: lignecommandeinterne fk_ligne_localsource; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lignecommandeinterne
    ADD CONSTRAINT fk_ligne_localsource FOREIGN KEY (idlocalsource) REFERENCES public.local(idlocal);


--
-- TOC entry 5010 (class 2606 OID 19224)
-- Name: mouvement fk_mouvement_article; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mouvement
    ADD CONSTRAINT fk_mouvement_article FOREIGN KEY (idarticle) REFERENCES public.article(idarticle) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5011 (class 2606 OID 19845)
-- Name: mouvement fk_mouvement_commandeexterne; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mouvement
    ADD CONSTRAINT fk_mouvement_commandeexterne FOREIGN KEY (idcommandeexterne) REFERENCES public.commandeexterne(idcommande) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 5012 (class 2606 OID 19315)
-- Name: mouvement fk_mouvement_commandeinterne; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mouvement
    ADD CONSTRAINT fk_mouvement_commandeinterne FOREIGN KEY (idcommandeinterne) REFERENCES public.commandeinterne(idcommande) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5013 (class 2606 OID 19701)
-- Name: mouvement fk_mouvement_local; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mouvement
    ADD CONSTRAINT fk_mouvement_local FOREIGN KEY (idlocal) REFERENCES public.local(idlocal) ON DELETE CASCADE;


--
-- TOC entry 5002 (class 2606 OID 19179)
-- Name: stock fk_stock_article; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock
    ADD CONSTRAINT fk_stock_article FOREIGN KEY (idarticle) REFERENCES public.article(idarticle) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5003 (class 2606 OID 19184)
-- Name: stock fk_stock_local; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock
    ADD CONSTRAINT fk_stock_local FOREIGN KEY (idlocal) REFERENCES public.local(idlocal) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5019 (class 2606 OID 19252)
-- Name: lignecommandeinterne lignecommandeinterne_idarticle_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lignecommandeinterne
    ADD CONSTRAINT lignecommandeinterne_idarticle_fkey FOREIGN KEY (idarticle) REFERENCES public.article(idarticle);


--
-- TOC entry 5020 (class 2606 OID 19247)
-- Name: lignecommandeinterne lignecommandeinterne_idcommandeinterne_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lignecommandeinterne
    ADD CONSTRAINT lignecommandeinterne_idcommandeinterne_fkey FOREIGN KEY (idcommandeinterne) REFERENCES public.commandeinterne(idcommande) ON DELETE CASCADE;


--
-- TOC entry 5021 (class 2606 OID 19286)
-- Name: ligneinventaire ligneinventaire_idarticle_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ligneinventaire
    ADD CONSTRAINT ligneinventaire_idarticle_fkey FOREIGN KEY (idarticle) REFERENCES public.article(idarticle);


--
-- TOC entry 5022 (class 2606 OID 19281)
-- Name: ligneinventaire ligneinventaire_idinventaire_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ligneinventaire
    ADD CONSTRAINT ligneinventaire_idinventaire_fkey FOREIGN KEY (idinventaire) REFERENCES public.inventaire(idinventaire) ON DELETE CASCADE;


--
-- TOC entry 5023 (class 2606 OID 19291)
-- Name: ligneinventaire ligneinventaire_idlocal_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ligneinventaire
    ADD CONSTRAINT ligneinventaire_idlocal_fkey FOREIGN KEY (idlocal) REFERENCES public.local(idlocal);


-- Completed on 2026-11-16 15:51:50

--
-- PostgreSQL database dump complete
--

\unrestrict MsZgXnWkiGU5XwniMCcLG9AwdNtlpU6CPOgkGv2G88tekPPd4Mgy70VTZbEOBmc

