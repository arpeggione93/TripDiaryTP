<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous" />
<link href="${pageContext.request.contextPath}/resources/css/style.css" 
	rel="stylesheet"/>
<c:if test="${empty darkmode}">
	<link href="${pageContext.request.contextPath}/resources/css/style.css" 
	rel="stylesheet"/>
</c:if>
<c:if test="${not empty darkmode}">
	<link href="${pageContext.request.contextPath}/resources/css/darkstyle.css" 
	rel="stylesheet"/>
</c:if>
<title>Trip Diary</title>

</head>

<body>
	<jsp:include page="../${pageContext.request.contextPath}/common/header.jsp" flush="false" />

	<div class="container">
		<h1>Pick!</h1>
		<br>
		
		<!-- 지역별 검색 기준, 태그검색을 위한 코드 -->
		<form name="search" method="get" class="row" style="justify-content: center;">
			<select id="place" name="place" class="form-select mb-3">
				<option selected disabled hidden>지역선택</option>
				<option value="seoul">서울특별시</option>
				<option value="busan">부산광역시</option>
				<option value="daegu">대구광역시</option>
				<option value="incheon">인천광역시</option>
				<option value="gwangju">광주광역시</option>
				<option value="daejeon">대전광역시</option>
				<option value="ulsan">울산광역시</option>
				<option value="sejong">세종특별자치시</option>
				<option value="gyeonggi">경기도</option>
				<option value="gangwon">강원도</option>
				<option value="chungbuk">충천북도</option>
				<option value="chungnam">충천남도</option>
				<option value="jeonbuk">전라북도</option>
				<option value="jeonnam">전라남도</option>
				<option value="gyeongbuk">경상북도</option>
				<option value="gyeongnam">경상남도</option>
				<option value="jeju">제주도특별자치도</option>
				<option value="abroad">해외</option>
			</select> 
			<div class="col-4">
				<input type="text" name="tag" id="tag" placeholder="태그검색" value="${tag}" class="form-control"> 
			</div >
			<div class="col-auto" style="padding-left: 0px">
				<input type="submit" value="검색하기" id="search" class="btn btn-primary" >
			</div>
		</form><br>
		
		<!-- 세션 저장된 값에 따라 글자를 다르게 표시 -->
		<c:choose>
			<c:when test="${sort eq 'regdate'}">
				<button class="btn btn-secondary" onclick='location.href="/pick?sort=regdate&place=${place}&tag=${tag}"'><b>작성일순</b>	</button>
				<button class="btn btn-outline-secondary" onclick='location.href="/pick?sort=tripdate&place=${place}&tag=${tag}"'>여행일순</button>
				<button class="btn btn-outline-secondary" onclick='location.href="/pick?sort=like&place=${place}&tag=${tag}"'>좋아요순</button>
			</c:when>
			<c:when test="${sort eq 'tripdate'}">
				<button class="btn btn-outline-secondary" onclick='location.href="/pick?sort=regdate&place=${place}&tag=${tag}"'>작성일순</button>
				<button class="btn btn-secondary" onclick='location.href="/pick?sort=tripdate&place=${place}&tag=${tag}"'><b>여행일순</b></button>
				<button class="btn btn-outline-secondary" onclick='location.href="/pick?sort=like&place=${place}&tag=${tag}"'>좋아요순</button>
			</c:when>
			<c:when test="${sort eq 'like'}">
				<button class="btn btn-outline-secondary" onclick='location.href="/pick?sort=regdate&place=${place}&tag=${tag}"'>작성일순</button>
				<button class="btn btn-outline-secondary" onclick='location.href="/pick?sort=tripdate&place=${place}&tag=${tag}"'>여행일순</button>
				<button class="btn btn-secondary" onclick='location.href="/pick?sort=like&place=${place}&tag=${tag}"'><b>좋아요순</b></button>
			</c:when>
		</c:choose>

		<!-- 전체 게시물 부분  -->
		<!-- 검색등으로 게시글에 대한 결과가 없을 경우 보여주는 화면 -->
		<c:if test="${pickPageList eq null}">
			<div class="container">
				<div class="container">
					<c:if test="${empty darkmode}">
						<img alt="" src="${pageContext.request.contextPath}/resources/img/notexist.png" style="width: 30%;">
					</c:if>
					<c:if test="${not empty darkmode}">
						<img alt="" src="${pageContext.request.contextPath}/resources/img/darknotexist.png" style="width: 30%;">
					</c:if>
				</div>
			</div>
		</c:if>

		<c:if test="${pickPageList ne null}">
			<div class="diary-mid row mt-5 mb-5">
				<c:forEach items="${pickPageList}" var="pickPageList">
					<!-- 게시물 1개 부분 이 주석 밑부분 부터 반복문 실행-->
					<div class="col-lg-4 diary-board-container">
						<div class="border border-secondary p-3 icon2">
							<div class="board-top">
								<div style="float: left;">
									<!-- 프로필 이미지와 닉네임 -->
									<c:if test="${pickPageList.profileStoreFileName ne null}">
										<img alt=""
											src="<spring:url value='/profile/${pickPageList.profileStoreFileName}'/>"
											class="border rounded-circle"
											style="width: 50px; height: 50px; object-fit: cover;">
									</c:if>
									<c:if test="${pickPageList.profileStoreFileName eq null}">
										<img alt="" src="resources/img/profile_48.png"
											class="border rounded-circle"
											style="width: 50px; height: 50px; object-fit: cover;">
									</c:if>
									<!-- 각 닉네임별 다이어리 페이지 이동 -->
									<a href="#" class="main-a">
										${pickPageList.nickname}
									</a>
								</div>

								<!-- pick 이미지 -->
								<div style="float: right; display: inline-block;" class="">
									<a href="/pickPageClick?pickNum=${pickPageList.pickNum}&memberNum=${authInfo.memberNum}&boardNum=${pickPageList.boardNum}"
										onclick="alert('찜하기가 취소되었습니다.')"> 
										<c:if test="${empty darkmode }">
											<img alt="" src="resources/img/pick_basic_dark.png" class=""
											style="width: 40px; height: 40px; object-fit: cover; margin-top: 5px;">
										</c:if>
										<c:if test="${not empty darkmode }">
											<img alt="" src="resources/img/pick_gray_on.png" class=""
											style="width: 40px; height: 40px; object-fit: cover; margin-top: 5px;">
										</c:if>
									</a>

									<!-- 세션이 없는경우 로그인으로 유도 -->
									<c:if test="${authInfo eq null}">
										<a href="/signIn" onclick="alert('로그인 후 사용가능합니다.')"> 
											<c:if test="${empty darkmode }">
												<img alt="" src="resources/img/pick_basic_white.png" class=""
												style="width: 40px; height: 40px; object-fit: cover; margin-top: 5px;">
											</c:if>
											<c:if test="${not empty darkmode }">
												<img alt="" src="resources/img/pick_gray_off.png" class=""
												style="width: 40px; height: 40px; object-fit: cover; margin-top: 5px;">
											</c:if>
										</a>
									</c:if>
								</div>
							</div>

							<!-- 썸네일 이미지 -->
							<div class="board-mid">
								<a href="/readView?boardNum=${pickPageList.boardNum}&memberNum=${pickPageList.memberNum}"> 
									<img class="image-thumbnail border border-secondary mt-3"
									src="<spring:url value='/thumbnail/${pickPageList.mainStoreFileName}'/>"
									style="width: 100%;">
								</a>
							</div>

							<!-- 하단 정보부분 -->
							<div class="board-bottom mt-5 mb-3">
								<div>
									여행날짜 : <fmt:formatDate value="${pickPageList.tripdate}" pattern="yyyy-MM-dd" />
								</div>
								<div>
									좋아요 ${pickPageList.tdLikeCnt}개
								</div>
								<div>
									<c:forEach items="${pickPageTagList}" var="pickPageTagList">
										<c:if test="${pickPageTagList.boardNum eq pickPageList.boardNum }">
											<c:choose>
												<c:when test="${place eq null}">
													<a href="/pick?tag=${pickPageTagList.tag}" style="color: #2883f3; text-decoration:none;">
														#${pickPageTagList.tag}
													</a>
												</c:when>
												<c:otherwise>
													<a href="/pick?place=${place}&tag=${pickPageTagList.tag}" style="color: #2883f3; text-decoration:none;">
														#${pickPageTagList.tag}
													</a>
												</c:otherwise>
											</c:choose>
										</c:if>
									</c:forEach>
								</div>
							</div>
						</div>
					</div>
				</c:forEach>
			</div>
		</c:if>

		
		<div class="col-md-offset-3">
			<nav aria-label="Page navigation example">
			<ul class="pagination pagination-lg mb-5" style="justify-content: center;">
				<c:if test="${paging.startPage != 1}">
					<li class="page-item">
						<a class="page-link" href="/main?page=${paging.startPage - 1}" aria-label="Previous">
							<span aria-hidden="true">&laquo;</span>
						</a>
					</li>
				</c:if>
				<c:forEach var="i" begin="${paging.startPage}" end="${paging.endPage}">
					<li class="page-item <c:if test="${ paging.page eq i}">active</c:if> ">
						<a class="page-link" href="/main?page=${i}">
							${i}
						</a>
					</li>
				</c:forEach>
				<c:if test="${paging.endPage != paging.lastPage}">
					<li class="page-item">
						<a class="page-link" href="/main?page=${paging.endPage + 1}" aria-label="Next">
							<span aria-hidden="true">&raquo;</span>
						</a>
					</li>
				</c:if>
			</ul>	
			</nav>		
		</div>
		
	</div>

	<jsp:include page="../${pageContext.request.contextPath}/common/sidebar.jsp" flush="false" />
</body>
</html>