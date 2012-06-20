--
-- Structure for table context
--

DROP TABLE IF EXISTS context;
CREATE TABLE context (
  id_context INT DEFAULT 0 NOT NULL,
  html LONG VARCHAR,
  nb_params INT DEFAULT 0 NOT NULL,
  priority INT DEFAULT 0 NOT NULL,
  strict SMALLINT DEFAULT 0 NOT NULL,
  active SMALLINT DEFAULT 0 NOT NULL,
  PRIMARY KEY (id_context)
);

--
-- Structure for table context_params
--
DROP TABLE IF EXISTS context_params;
CREATE TABLE context_params (
  id_context INT DEFAULT 0 NOT NULL,
  param_key VARCHAR(100) DEFAULT '' NOT NULL,
  param_value VARCHAR(100) DEFAULT '' NOT NULL
);
