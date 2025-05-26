use tradedb;
CREATE TABLE tradedb.trade_det (
    trade_id VARCHAR(50) NOT NULL,
    version int not null,

     counter_party_id VARCHAR(50) NOT NULL,
     book_id varchar(50) not null,
     maturity_date date not null,
     created_date date not null,
     is_expired char(1) not null default 'N',
       PRIMARY KEY(trade_id)
);

INSERT INTO tradedb.trade_det VALUES('1', 1, '121', '12321', CURRENT_DATE+10, CURRENT_DATE, 'N');
INSERT INTO tradedb.trade_det VALUES('2', 3, '333', '313131', CURRENT_DATE+10, CURRENT_DATE, 'N');
INSERT INTO tradedb.trade_det VALUES('1', 2, '121', '12321', CURRENT_DATE+10, CURRENT_DATE, 'N');
INSERT INTO tradedb.trade_det VALUES('1', 1, '121', '12321', CURRENT_DATE+10, CURRENT_DATE, 'N');