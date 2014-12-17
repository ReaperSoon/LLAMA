# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table bill (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  user_login                varchar(255),
  shipment_address          varchar(255),
  buyer_name                varchar(255),
  date                      datetime,
  total                     float,
  constraint pk_bill primary key (id))
;

create table bill_line (
  id                        bigint auto_increment not null,
  product_id                bigint,
  product_name              varchar(255),
  quantity                  integer,
  product_price             float,
  bill_id                   bigint,
  constraint pk_bill_line primary key (id))
;

create table comment (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  user_login                varchar(255),
  text                      varchar(255),
  rating                    float,
  date                      datetime,
  product_id                bigint,
  constraint pk_comment primary key (id))
;

create table product (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  description               varchar(255),
  price                     float,
  quantity                  integer,
  constraint pk_product primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
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
alter table comment add constraint fk_comment_product_1 foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_comment_product_1 on comment (product_id);



alter table bill_bill_line add constraint fk_bill_bill_line_bill_01 foreign key (bill_id) references bill (id) on delete restrict on update restrict;

alter table bill_bill_line add constraint fk_bill_bill_line_bill_line_02 foreign key (bill_line_id) references bill_line (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table bill;

drop table bill_bill_line;

drop table bill_line;

drop table comment;

drop table product;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

