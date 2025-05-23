
CREATE TABLE organization (
                              organizationId BIGINT AUTO_INCREMENT PRIMARY KEY,
                              name VARCHAR(255) NOT NULL,
                              email VARCHAR(255),
                              address VARCHAR(255),
                              region ENUM('SEOUL', 'BUSAN', 'DAEGU', 'INCHEON',
                                  'GWANGJU', 'DAEJEON', 'ULSAN', 'SEJONG', 'SUWON', 'GOYANG', 'YONGIN',
                                  'SEONGNAM', 'BUCHEON', 'ANSAN', 'NAMYANGJU', 'ANYANG', 'HWASEONG',
                                  'PYEONGTAEK', 'UIJEONGBU', 'SIHEUNG', 'PAJU', 'GIMPO', 'GWANGMYEONG',
                                  'G_GWANGJU', 'GUNPO', 'OSAN', 'ICHEON', 'YANGJU', 'ANSEONG', 'GURI',
                                  'POCHEON', 'UIWANG', 'HANAM', 'YEOJU', 'DONGDUCHEON', 'GWACHEON',
                                  'GAPYEONG', 'YEONCHEON', 'YANGPYEONG', 'GANGWON', 'CHUNGBUK',
                                  'CHUNGNAM', 'JEONBUK', 'JEONNAM', 'GYEONGBUK', 'GYEONGNAM', 'JEJU') NOT NULL,
                              url varchar(255) NOT NULL
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

INSERT INTO organization(name, email, address, region, url) values('대한의료사회복지사협회','','서울특별시 강서구 마곡동 공항대로 209 GMG엘스타 918호','SEOUL', 'https://kamsw.or.kr');
INSERT INTO organization(name, email, address, region, url) values('한국노인인력개발원','','경기도 고양시 일산동구 중산동 하늘마을로 106','GOYANG', 'https://www.kordi.or.kr');
INSERT INTO organization(name, email, address, region, url) values('경기도사회복지사협회','','경기도 수원시 영통구 덕영대로1556번길 16 KR 디지털엠파이어 B동 406호','SUWON', 'https://www.ggsw.kr');
INSERT INTO organization(name, email, address, region, url) values('오정노인복지관','','경기도 부천시 오정구 소사로 669번길 10','BUCHEON', 'https://senior.bucheon4u.kr');
INSERT INTO organization(name, email, address, region, url) values('원미노인복지관','','경기도 부천시 원미구 부천로136번길 27','BUCHEON', 'https://senior.bucheon4u.kr');
INSERT INTO organization(name, email, address, region, url) values('소사노인복지관','','경기도 부천시 소사구 경인옛로 73 소사구청','BUCHEON', 'https://senior.bucheon4u.kr');
INSERT INTO organization(name, email, address, region, url) values('부천시니어클럽','','경기도 부천시 부일로 406','BUCHEON', 'https://senior.bucheon4u.kr');
INSERT INTO organization(name, email, address, region, url) values('소사본종합사회복지관','','경기도 부천시 호현로489번길 50','BUCHEON', 'https://welfare.bucheon4u.kr');
INSERT INTO organization(name, email, address, region, url) values('상동종합사회복지관','','경기도 부천시 석천로16번길 50','BUCHEON', 'https://welfare.bucheon4u.kr');
INSERT INTO organization(name, email, address, region, url) values('대산종합사회복지관','','경기도 부천시 소사구 심곡로9번길54','BUCHEON', 'https://welfare.bucheon4u.kr');
INSERT INTO organization(name, email, address, region, url) values('춘의종합사회복지관','','경기도 부천시 원미구 원미로 202','BUCHEON', 'https://welfare.bucheon4u.kr');
INSERT INTO organization(name, email, address, region, url) values('심곡동종합사회복지관','','경기도 부천시 원미구 장말로 351번길','BUCHEON', 'https://welfare.bucheon4u.kr');
INSERT INTO organization(name, email, address, region, url) values('인천종합사회복지관','','인천광역시 미추홀구 매소홀로418번길','INCHEON', 'http://www.icwelfare.or.kr');
INSERT INTO organization(name, email, address, region, url) values('인천광역시장애인종합복지관','','인천광역시 연수구 앵고개로 130','INCHEON', 'https://icjb.or.kr');
INSERT INTO organization(name, email, address, region, url) values('인천광역시사회복지사협회','','인천광역시 남동구 용천로 208','INCHEON', 'https://www.iasw.or.kr');
INSERT INTO organization(name, email, address, region, url) values('미추홀장애인종합복지관','','인천광역시 미추홀구 경원대로 714','INCHEON', 'https://icjb.or.kr');
INSERT INTO organization(name, email, address, region, url) values('서울시사회복지사협회','sasw@sasw.or.kr','서울특별시 영등포구 당산로 171','SEOUL', 'https://sasw.or.kr');

CREATE TABLE news (
                      newsId BIGINT AUTO_INCREMENT PRIMARY KEY,
                      baseId BIGINT,
                      title VARCHAR(255) NOT NULL,
                      type ENUM('NOTICE', 'WELFARE', 'RECRUIT', 'EVENT') NOT NULL,
                      link VARCHAR(255) NOT NULL,
                      organizationId BIGINT,
                      createdAt DATETIME,

                      FOREIGN KEY (organizationId) REFERENCES organization(organizationId)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE TABLE member (
                        memberId BIGINT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(255) NOT NULL UNIQUE ,
                        password VARCHAR(255) NULL,
                        nickname VARCHAR(255) NOT NULL ,
                        role ENUM('USER', 'ADMIN') NOT NULL ,
                        provider ENUM('KAKAO'),
                        providerId VARCHAR(255)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;