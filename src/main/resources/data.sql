# INSERT INTO Member (member_id, username, password, email, nickname, authority, reportedStatus, createdAt, modifiedAt)
# VALUES
# (1, 'kimsb7218', 'daily1313!' , 'kimsb7219@naver.com', '고릴라', 'ROLE_USER' , false, now(), now()),
# (2, 'kimsb7219', 'daily1314!' , 'kimsb7220@naver.com', '이쁜이', 'ROLE_USER' , false, now(), now()),
# (3, 'kimsb7220', 'daily1315!' , 'kimsb7221@naver.com', '비실이', 'ROLE_USER' , false, now(), now()),
# (4, 'kimsb7221', 'daily1316!' , 'kimsb7222@naver.com', '은지렁이', 'ROLE_USER' , false, now(), now()),
# (5, 'kimsb7222', 'daily1318!' , 'kimsb7223@naver.com', '삼다수', 'ROLE_USER' , false, now(), now()),
# (6, 'kimsb7223', 'daily1319!' , 'kimsb7224@naver.com', '삼다수1', 'ROLE_USER' , false, now(), now()),
# (7, 'kimsb7224', 'daily1320!' , 'kimsb7225@naver.com', '삼다수2', 'ROLE_ADMIN' , false, now(), now()),
# (8, 'kimsb7225', 'daily1321!' , 'kimsb7226@naver.com', '삼다수3', 'ROLE_USER' , false, now(), now()),
# (9, 'kimsb7226', 'daily1322!' , 'kimsb7227@naver.com', '삼다수4', 'ROLE_USER' , false, now(), now()),
# (10, 'kimsb7227', 'daily1323!' , 'kimsb7228@naver.com', '삼다수5', 'ROLE_USER' , false, now(), now());
#
#
# INSERT INTO match_table (member_id, category, matchName, address, start_date, end_date, detailsContent, price, precaution, isMatched, reportedStatus, createdAt, modifiedAt)
# VALUES
#     (1, '노인돌봄', 'Match 1', 'Address 1', '2023-05-15T10:00', '2023-05-15T12:00', 'Details 1', 10000, 'Precaution 1', false, false, now(), now()),
#     (1, '아이돌봄', 'Match 2', 'Address 2', '2023-05-16T14:00', '2023-05-16T16:00', 'Details 2', 20000, 'Precaution 2', false, false, now(), now()),
#     (1, '반려동물', 'Match 3', 'Address 3', '2023-05-17T09:00', '2023-05-17T11:00', 'Details 3', 15000, 'Precaution 3', false, false, now(), now()),
#     (2, '노인돌봄', 'Match 4', 'Address 3', '2023-05-17T09:00', '2023-05-17T11:00', 'Details 3', 150000, 'Precaution 3', false, false, now(), now()),
#     (2, '아이돌봄', 'Match 5', 'Address 3', '2023-05-17T09:00', '2023-05-17T11:00', 'Details 3', 160000, 'Precaution 3', false, false, now(), now()),
#     (2, '반려동물', 'Match 6', 'Address 3', '2023-05-17T09:00', '2023-05-17T11:00', 'Details 3', 170000, 'Precaution 3', false, false, now(), now()),
#     (3, '노인돌봄', 'Match 7', 'Address 3', '2023-05-17T09:00', '2023-05-17T11:00', 'Details 3', 180000, 'Precaution 3', false, false, now(), now()),
#     (3, '아이돌봄', 'Match 8', 'Address 3', '2023-05-17T09:00', '2023-05-17T11:00', 'Details 3', 190000, 'Precaution 3', false, false, now(), now()),
#     (3, '반려동물', 'Match 9', 'Address 3', '2023-05-17T09:00', '2023-05-17T11:00', 'Details 3', 200000, 'Precaution 3', false, false, now(), now());
#
# INSERT INTO MatchComment (id, writer_id, match_id, content, createdAt, modifiedAt)
# VALUES
#     (1, 1, 1, '첫 번째 댓글입니다.', now(), now()),
#     (2, 1, 2, '두 번째 댓글입니다.', now(), now()),
#     (3, 2, 1, '세 번째 댓글입니다.', now(), now()),
#     (4, 2, 2, '네 번째 댓글입니다.', now(), now()),
#     (5, 3, 1, '다섯 번째 댓글입니다.', now(), now()),
#     (6, 3, 2, '여섯 번째 댓글입니다.', now(), now()),
#     (7, 1, 3, '일곱 번째 댓글입니다.', now(), now()),
#     (8, 2, 3, '여덟 번째 댓글입니다.', now(), now()),
#     (9, 3, 3, '아홉 번째 댓글입니다.', now(), now()),
#     (10, 1, 4, '열 번째 댓글입니다.', now(), now());
#
# INSERT INTO MatchFavorite (match_id, member_id, createdAt, modifiedAt)
# VALUES
#     (4, 10, now(), now()),
#     (2, 10, now(), now()),
#     (1, 10, now(), now()),
#     (1, 5, now(), now()),
#     (3, 5, now(), now()),
#     (4, 5, now(), now()),
#     (5, 6, now(), now()),
#     (6, 6, now(), now()),
#     (1, 6, now(), now());
#
#
# INSERT INTO Message (title, content, sender_id, receiver_id, deletedBySender, deletedByReceiver, createdAt, modifiedAt)
# VALUES
#     ('제목1', '내용1', 1, 2, false, false, now(), now()),
#     ('제목2', '내용2', 1, 3, false, false, now(), now()),
#     ('제목3', '내용3', 2, 1, false, false, now(), now()),
#     ('제목4', '내용4', 2, 3, false, false, now(), now()),
#     ('제목5', '내용5', 3, 1, false, false, now(), now()),
#     ('제목6', '내용6', 1, 2, false, false, now(), now()),
#     ('제목7', '내용7', 1, 3, false, false, now(), now()),
#     ('제목8', '내용8', 1, 4, false, false, now(), now()),
#     ('제목9', '내용9', 1, 5, false, false, now(), now()),
#     ('제목10', '내용10', 1, 6, false, false, now(), now()),
#     ('제목11', '내용11', 1, 7, false, false, now(), now());
#
# INSERT INTO Category (name)
# VALUES
#     ('카테고리1'),
#     ('카테고리2'),
#     ('카테고리3'),
#     ('카테고리4'),
#     ('카테고리5'),
#     ('카테고리6'),
#     ('카테고리7'),
#     ('카테고리8'),
#     ('카테고리9'),
#     ('카테고리10');
#
# SET FOREIGN_KEY_CHECKS = 0;
#
# INSERT INTO Board (title, content, member_id, category_id, reportedStatus, createdAt, modifiedAt)
# VALUES
#     ('첫 번째 게시글', '첫 번째 게시글 내용입니다.', 1, 1, false, now(), now()),
#     ('두 번째 게시글', '두 번째 게시글 내용입니다.', 2, 2, false, now(), now()),
#     ('세 번째 게시글', '세 번째 게시글 내용입니다.', 3, 3, false, now(), now()),
#     ('네 번째 게시글', '네 번째 게시글 내용입니다.', 4, 4, false, now(), now()),
#     ('다섯 번째 게시글', '다섯 번째 게시글 내용입니다.', 5, 5, false, now(), now()),
#     ('게시글 제목1', '게시글 내용1', 6, 6, false, now(), now()),
#     ('게시글 제목2', '게시글 내용2', 7, 7, false, now(), now()),
#     ('게시글 제목3', '게시글 내용3', 8, 8, false, now(), now()),
#     ('게시글 제목4', '게시글 내용4', 9, 9, false, now(), now()),
#     ('게시글 제목5', '게시글 내용5', 10, 10, false, now(), now());
#
# -- Image 더미 데이터 생성
# INSERT INTO Image (uniqueName, originName, board_id, createdAt, modifiedAt)
# VALUES
#     ('image1.jpg', 'image1.jpg', 1, now(), now()),
#     ('image2.jpg', 'image2.jpg', 2, now(), now()),
#     ('image3.jpg', 'image3.jpg', 3, now(), now()),
#     ('image4.jpg', 'image4.jpg', 4, now(), now()),
#     ('image5.jpg', 'image5.jpg', 5, now(), now()),
#     ('unique_name_1', 'origin_name_1.jpg', 6, now(), now()),
#     ('unique_name_2', 'origin_name_2.jpg', 7, now(), now()),
#     ('unique_name_3', 'origin_name_3.jpg', 8, now(), now()),
#     ('unique_name_4', 'origin_name_4.jpg', 9, now(), now()),
#     ('unique_name_5', 'origin_name_5.jpg', 10, now(), now());
#
#
# INSERT INTO BoardReport (reporter_id, reported_id, content, createdAt, modifiedAt)
# VALUES
#     (1, 2, 'Report Content 1', now(), now()),
#     (2, 3, 'Report Content 2', now(), now()),
#     (3, 4, 'Report Content 3', now(), now()),
#     (4, 5, 'Report Content 4', now(), now()),
#     (5, 6, 'Report Content 5', now(), now()),
#     (6, 7, 'Report Content 6', now(), now()),
#     (7, 8, 'Report Content 7', now(), now()),
#     (8, 9, 'Report Content 8', now(), now()),
#     (9, 10, 'Report Content 9', now(), now()),
#     (10, 1, 'Report Content 10', now(), now());
#
# INSERT INTO MatchReport (reporter_id, reported_id, content, createdAt, modifiedAt)
# VALUES
#     (1, 3, 'Match Report Content 1', now(), now()),
#     (2, 2, 'Match Report Content 2', now(), now()),
#     (3, 3, 'Match Report Content 3', now(), now()),
#     (4, 4, 'Match Report Content 4', now(), now()),
#     (5, 5, 'Match Report Content 5', now(), now()),
#     (6, 6, 'Match Report Content 6', now(), now()),
#     (7, 7, 'Match Report Content 7', now(), now()),
#     (8, 8, 'Match Report Content 8', now(), now()),
#     (9, 9, 'Match Report Content 9', now(), now()),
#     (10, 10, 'Match Report Content 10', now(), now());
#
# -- MemberReport 더미데이터 생성
# INSERT INTO MemberReport (reporter_id, reported_id, content, createdAt, modifiedAt)
# VALUES
#     (2, 1, 'Member Report Content 1', now(), now()),
#     (3, 2, 'Member Report Content 2', now(), now()),
#     (4, 3, 'Member Report Content 3', now(), now()),
#     (5, 4, 'Member Report Content 4', now(), now()),
#     (6, 5, 'Member Report Content 5', now(), now()),
#     (1, 6, 'Member Report Content 6', now(), now()),
#     (1, 7, 'Member Report Content 7', now(), now()),
#     (2, 8, 'Member Report Content 8', now(), now()),
#     (5, 9, 'Member Report Content 9', now(), now()),
#     (6, 10, 'Member Report Content 10', now(), now());
#
#
#



