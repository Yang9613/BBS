 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
    
<div class="pagination">
	<ul>
		<li><a href="article?action=page&curpage=1">首页</a></li>

		<li>
			<c:if test="${pb.curPage<=1}">
				<a href="">上一页</a>
			</c:if>
			<c:if test="${pb.curPage>1}">
				<a href="article?action=page&curpage=${pb.curPage-1}">上一页</a>
			</c:if>
		</li>
		
		
		<c:forEach var="i" step="1" begin="1" end="${pb.maxPage}">
			<li><a href="article?action=page&curpage=${i}">${i}</a></li>

		</c:forEach>
			<li><a href=""></a></li>
		
		
		
		
		<li>
			<c:if test="${pb.curPage==pb.maxPage}">
				下一页
			</c:if>
			<c:if test="${pb.curPage<pb.maxPage}">
				<a href="article?action=page&curpage=${pb.curPage+1}">下一页</a>
			</c:if>


		</li>



		<li><a href="article?action=page&curpage=${pb.maxPage}">尾页</a></li>
	</ul>
</div>