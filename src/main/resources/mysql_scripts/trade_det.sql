use tradedb;
CREATE TABLE tradedb.trade_det (
    trade_id character varying(50) NOT NULL,
    version numeric(15) not null,

     counter_party_id character varying(50) NOT NULL,
     book_id character varying(50) not null,
     maturity_date date not null,
     created_date date not null,
     is_expired character varying(1) not null default 'N',
       CONSTRAINT trade_det_Pkey PRIMARY KEY(trade_id)
);


INSERT INTO tradedb.trade_det VALUES('1', 1, '121', '12321', CURRENT_DATE+10, CURRENT_DATE, 'N');
INSERT INTO tradedb.trade_det VALUES('2', 3, '333', '313131', CURRENT_DATE+10, CURRENT_DATE, 'N');
INSERT INTO tradedb.trade_det VALUES('1', 2, '121', '12321', CURRENT_DATE+10, CURRENT_DATE, 'N');
INSERT INTO tradedb.trade_det VALUES('1', 1, '121', '12321', CURRENT_DATE+10, CURRENT_DATE, 'N');