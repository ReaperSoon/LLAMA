# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table bill (
  id                        bigint not null,
  user_id                   bigint,
  user_login                varchar(255),
  shipment_address          varchar(255),
  buyer_name                varchar(255),
  date                      timestamp,
  total                     float,
  constraint pk_bill primary key (id))
;

create table bill_line (
  id                        bigint not null,
  product_id                bigint,
  product_name              varchar(255),
  quantity                  integer,
  product_price             float,
  bill_id                   bigint,
  constraint pk_bill_line primary key (id))
;

create table comment (
  id                        bigint not null,
  user_id                   bigint,
  user_login                varchar(255),
  text                      varchar(255),
  rating                    float,
  date                      timestamp,
  product_id                bigint,
  constraint pk_comment primary key (id))
;

create table product (
  id                        bigint not null,
  name                      varchar(255),
  description               varchar(255),
  price                     float,
  quantity                  integer,
  constraint pk_product primary key (id))
;

create table user (
  id                        bigint not null,
  mail                      varchar(255),
  login                     varchar(255),
  password                  varchar(255),
  is_admin                  integer,
  constraint pk_user primary key (id))
;


create table bill_bill_line (
  bill_id                        bigint not null,
  bill_line_id                   bigint not null,
  constraint pk_bill_bill_line primary key (bill_id, bill_line_id))
;
create sequence bill_seq;

create sequence bill_line_seq;

create sequence comment_seq;

create sequence product_seq;

create sequence user_seq;

alter table comment add constraint fk_comment_product_1 foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_comment_product_1 on comment (product_id);



alter table bill_bill_line add constraint fk_bill_bill_line_bill_01 foreign key (bill_id) references bill (id) on delete restrict on update restrict;

alter table bill_bill_line add constraint fk_bill_bill_line_bill_line_02 foreign key (bill_line_id) references bill_line (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists bill;

drop table if exists bill_bill_line;

drop table if exists bill_line;

drop table if exists comment;

drop table if exists product;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists bill_seq;

drop sequence if exists bill_line_seq;

drop sequence if exists comment_seq;

drop sequence if exists product_seq;

drop sequence if exists user_seq;

