--
-- Structure for table contextinclude_context
--

DROP TABLE IF EXISTS contextinclude_context;
CREATE TABLE contextinclude_context (
  id_context INT DEFAULT 0 NOT NULL,
  html LONG VARCHAR,
  nb_params INT DEFAULT 0 NOT NULL,
  priority INT DEFAULT 0 NOT NULL,
  strict SMALLINT DEFAULT 0 NOT NULL,
  active SMALLINT DEFAULT 0 NOT NULL,
  PRIMARY KEY (id_context)
);

--
-- Structure for table contextinclude_context_params
--
DROP TABLE IF EXISTS contextinclude_context_params;
CREATE TABLE contextinclude_context_params (
  id_context INT DEFAULT 0 NOT NULL,
  param_key VARCHAR(100) DEFAULT '' NOT NULL,
  param_value VARCHAR(100) DEFAULT '' NOT NULL
);

--
-- Structure for table contextinclude_parameter
--
DROP TABLE IF EXISTS contextinclude_parameter;
CREATE TABLE contextinclude_parameter (
	parameter_key varchar(255) NOT NULL,
	parameter_value varchar(255) NOT NULL,
	PRIMARY KEY (parameter_key)
);
