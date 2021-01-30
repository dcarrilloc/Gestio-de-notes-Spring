<%--
  Created by IntelliJ IDEA.
  User: Dani
  Date: 11/11/2020
  Time: 22:48
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="myUtils" class="com.esliceu.utils.Util"/>
<html>
<head>
    <title>Note</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
</head>
<body style="margin: 0; padding: 0; box-sizing: border-box; width: 100%; background-color: #303030; color: white;">

<jsp:include page="header.jsp" />

<c:choose>
    <c:when test="${not empty note}">
        <div style="width: 40%; margin: 200px auto;">
            <c:set var="isowner" value="${ownership}"/>
            <c:if test="${isowner == false}">
                <fieldset disabled>
            </c:if>
                <a href="${pageContext.request.contextPath}/edit?id=${note.noteid}" style="cursor: pointer; text-decoration: none; color: white;">
                    <div style="background-color: #424242 !important; border-radius: 5px;">
                        <div class="form-group" style="height: 50px; padding: 1rem 2rem 0 2rem;">
                            <h5 style="background-color: #424242 !important; border: none; color: white; font-size: larger; width: 100%; outline: none; text-overflow: ellipsis; white-space:nowrap; overflow:hidden">${note.title}</h5>
                        </div>
                        <div class="form-group" style="padding: 1rem 2rem 1rem 2rem; overflow-wrap: anywhere;">
                                ${note.body}
                        </div>
                    </div>
                </a>
            <c:if test="${isowner == false}">
                <small style="margin-bottom: 15px;" id="emailHelp" class="form-text text-muted">You are not owner so you can't edit or share this note. If you don't want to see this note you can delete it from your feed.</small>
                </fieldset>
            </c:if>

            <form class="form-inline" action="${pageContext.request.contextPath}/shareNote" method="POST" style="display: flex; flex-flow: row wrap; width: 100%;">
                <c:if test="${isowner == false}">
                    <fieldset disabled>
                </c:if>
                <div class="input-group mb-3" style="margin-bottom: 0 !important; margin-right: 20px;">
                    <div class="input-group-prepend">
                        <label class="input-group-text" for="inputGroupSelect01">Share with</label>
                    </div>
                    <select class="custom-select" id="inputGroupSelect01" name="option">
                        <option selected>Choose...</option>
                        <c:forEach var="c" items="${users}">
                            <option value="${c}">${c}</option>
                        </c:forEach>
                    </select>
                </div>
                <input type="hidden" name="noteid" value="${note.noteid}">
                <input type="hidden" name="_csrftoken" value="${csrfToken}">
                <button type="submit" class="btn btn-primary my-1">Share</button>
                <c:if test="${isowner == false}">
                    </fieldset>
                </c:if>
            </form>
            <c:if test="${isowner == false}">
                </fieldset>
            </c:if>

            <c:if test="${not empty usersShared}">
                <div style="display: flex; flex-flow: row wrap; width: 100%; align-items: center;">
                    <p style="margin-right: 10px;">Shared with: </p>
                    <c:forEach var="c" items="${usersShared}">
                        <form action="/deleteUserFromSharedNote" method="POST" style="display: flex; flex-flow: row wrap; align-items: center; padding: 5px; background-color: #424242 !important; align-self: center; vertical-align: middle; border: 1px solid rgba(255, 255, 255, 0.6); border-radius: 10px; margin-right: 10px;">
                            <input type="hidden" name="username" value="${c.username}">
                            <input type="hidden" name="noteid" value="${note.noteid}">
                            <input type="hidden" name="_csrftoken" value="${csrfToken}">
                            <p style="margin: auto 7px;">@${c.username}</p>
                            <button type="submit" value="Submit" style="border-radius: 100%; background-color: transparent; border: none;">
                                <svg width="15px" height="15px" viewBox="0 0 16 16" class="bi bi-x-circle" fill="white" xmlns="http://www.w3.org/2000/svg">
                                    <path fill-rule="evenodd" d="M8 15A7 7 0 1 0 8 1a7 7 0 0 0 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>
                                    <path fill-rule="evenodd" d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z"/>
                                </svg>
                            </button>
                        </form>
                    </c:forEach>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/deleteNote" method="POST">
                <input type="hidden" name="notesToDelete" value="${note.noteid}">
                <input type="hidden" name="_csrftoken" value="${csrfToken}">

                <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#exampleModalCenter" style="display: flex; flex-flow: row nowrap; align-items: center;">
                    Delete
                    <svg width="1em" height="1em" viewBox="0 0 16 16" class="bi bi-trash-fill" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                        <path fill-rule="evenodd" d="M2.5 1a1 1 0 0 0-1 1v1a1 1 0 0 0 1 1H3v9a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V4h.5a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1H10a1 1 0 0 0-1-1H7a1 1 0 0 0-1 1H2.5zm3 4a.5.5 0 0 1 .5.5v7a.5.5 0 0 1-1 0v-7a.5.5 0 0 1 .5-.5zM8 5a.5.5 0 0 1 .5.5v7a.5.5 0 0 1-1 0v-7A.5.5 0 0 1 8 5zm3 .5a.5.5 0 0 0-1 0v7a.5.5 0 0 0 1 0v-7z"/>
                    </svg>
                </button>

                <!-- Modal -->
                <div class="modal fade" id="exampleModalCenter" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="exampleModalLongTitle" style="color: black;">Please confirm</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <c:choose>
                                <c:when test="${isowner == false}">
                                    <div class="modal-body" style="color: black;">
                                        You will not see this note.
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="modal-body" style="color: black;">
                                        You're going to delete this note permanently.
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                <c:choose>
                                    <c:when test="${isowner == false}">
                                        <button type="submit" class="btn btn-danger">Delete</button>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="submit" class="btn btn-danger">Delete permanently</button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </c:when>
    <c:otherwise>
        <div style="width: 40%; margin: 150px auto;">
            <form action="${pageContext.request.contextPath}/createNote" method="POST">
                <div style="background-color: #424242 !important; border-radius: 5px;">
                    <div class="form-group" style="height: 50px; padding: 1rem 2rem 0 2rem;">
                        <input type="text" id="exampleFormControlInput2" name="title" placeholder="Title" style="background-color: #424242 !important; border: none; color: white; font-size: larger; width: 100%; outline: none;">
                    </div>
                    <div class="form-group" style="padding: 1rem 2rem 0 2rem;">
                        <textarea id="exampleFormControlTextarea2" rows="15" name="body" placeholder="Body" style="background-color: #424242 !important; border: none; color: white; width: 100%; outline: none;"></textarea>
                    </div>
                </div>
                <input type="hidden" name="_csrftoken" value="${csrfToken}">
                <button type="submit" class="btn btn-success">Create</button>
            </form>
        </div>
    </c:otherwise>
</c:choose>
</body>
<script
        src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"
></script>
<script
        src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"
></script>
<script
        src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"
></script>
</html>
