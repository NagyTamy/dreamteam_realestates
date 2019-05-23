<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,700" rel="stylesheet">
    <script src="index.js"></script>
    <script src="home-page.js"></script>
    <script src="realestate.js"></script>
    <title>DreamTeam Houses</title>
</head>
<body>
<!-- HEADER -->
    <header>
        <svg id="hide">
            <defs>
            </defs>
            <clipPath id="right-header-svg">
                <path class="st0" d="M1337,0v399L94.4,400c31.4-44.7,49.9-99.2,49.9-158C144.3,137.4,85.9,46.5,0,0H1337z"/>
            </clipPath>
        </svg>
        <div id="left-header-box">
            <div id="header-image"></div>
        </div>
        <div id="right-header-box">
            <div id="header-text">
                <h2>Lorem Ipsum</h2>
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus sagittis nulla eget libero commodo hendrerit. Etiam dapibus eleifend odio, condimentum ornare metus commodo eget. Donec porttitor nec urna ut aliquam. Aliquam dolor nisl, tincidunt nec velit eu, tempus facilisis diam. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus sagittis nulla eget libero commodo hendrerit. Etiam dapibus eleifend odio, condimentum ornare metus commodo eget. Donec porttitor nec urna ut aliquam. Aliquam dolor nisl, tincidunt nec velit eu, tempus facilisis diam. </p>
            </div>
        </div>
<!-- NAV -->
        <nav id="menu">
        </nav>
        <form id="header-form">
            <input type="text" name="seach-key" placeholder="Comma separated list of expressions...">
            <button id="search-button">Search</button>
            <button onclick="loadFilters()">Filters</button>
        </form>
    </header>
<!-- CONTAINER FOR MAIN CONTENT -->
    <div id="container" class="hidden content">
    </div>
    <footer>
        <img src="img/dark-angled-ring-w-bg.svg" alt="ring">
        <img src="img/dark-angled-ring-w-bg.svg" alt="ring">
        <img src="img/dark-angled-ring-w-bg.svg" alt="ring">
        <div id="footer-links">
            <ul>
                <li>Impressum</li>
                <li>About us</li>
                <li>How does it work?</li>
                <li>FAQ</li>
            </ul>
        </div>
    </footer>

</body>
</html>



