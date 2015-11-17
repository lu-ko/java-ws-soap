
    alter table Hotel 
        drop constraint FK42AD19433B9B256;

    alter table Package 
        drop constraint FK331DCC26AE667F62;

    drop table Destination if exists;

    drop table Hotel if exists;

    drop table Package if exists;
