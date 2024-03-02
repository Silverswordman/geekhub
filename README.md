<h1>💻🎮GeekHub🎮💻</h1>

Portal to explore italian conventions and events of the geek commuity.
Find your perfect event!
<hr>
️This web page is to explore the scene of italian comic conventions and pop-culture events , allows the user to search
by region , title of the convention and add them to the favorites, allows also to watch what they have to offer by
thematic areas and the events and services that are in the those areas.

Users can be upgraded to the event planner status which allows them to create, edit and delete the conventions and all
of their informations that they want to share with the community.

The application was developed using SpringBoot for the back-end and PostgreSQL for the database while the front-end uses
React-Redux.

<li>Link to the Front End https://github.com/Silverswordman/GeekHub-FE </li>

<li>Link to the Back End https://github.com/Silverswordman/geekhub</li>
<hr>

<h2><center>⚙️Functionalities⚙️</h4>
<ul>
<li>User Registration and Login with Token </li>
<li>Homepage with carousel of the next incoming events by today's date and general registered conventions with also filters by region and title.</li>
<li>Convention detail page with all the infos and their thematic areas listed beside.</li>
<li>Thematic Areas detail page with all the events and services listed.</li>
<li>Normal User  action to add&remove from favorites.</li>
<li>Normal User action to request an upgrade to the Admin for the Event Planner Status.</li>
<li>Event Planner User  actions to upload cover photo , logo and edit and delete conventions they  created</li>
<li>Event Planner User  actions to add /edit and delete new thematic areas to their conventions and subsections to the thematic areas.</li>
<li>Admin page to accept or decline request of upgrading users.
<li>Personal Profile Page with uploading avatar and favorites list.</ul>


<hr>
<h3>📦Installed Packages (Front End)📦</h3>
<li>Project Created with Vite 5.1.0. </li>
<li>Packages installed</li>
<li>React 18.2.0</li>
<li>React Redux 9.1.0</li>
<li>React-Bootstrap 2.10.1</li>
<li>Bootstrap 5.3.2</li>
<li>Date-fns 3.3.1</li>
<li>React-Icons 5.0.1</li>
<li>React-Router-Dom 6.21.3</li>
<li>React-datepicker 6.1.0</li>
<li>Vite-Plugin-Sass 01.0</li>
<hr>

<h3>🟥Maven Installed (Back-End)🟥</h3>
<li>Spring Boot Starter Data</li>
<li>Spring Boot Starter Security</li>
<li>Spring Boot Starter Web</li>
<li>Sprin Boot DevTools</li>
<li>Lombok</li>
<li>PostgreSQL</li>
<li>JsonWebToken: jjwt-jackson,jjwt-impl,jjwt-api.</li>
<li>Spring Boot Starter Validation</li>
<li>Cloudinary-HTTP44 1.37.0 ver</li>
<li>Apache Commons-CSV reader 1.5 ver</li>
<hr>
<h3>️️<center>🏃‍♂Running the Application🏃</center></h3>

**_Front End_**
Run npm-install and then npm run dev , navigate to http://localhost:5173/.

**_Back End_**
Compile an env.properties following env.example directions.
Run the CSV import in the Configuration folder for the import of the Regions,Provinces and Cities in the database and
then close the import.
Run the Application.
Hard set-up an Admin User via database for full functionality.
<hr>

<h3>**_🖋️Author🖋️_**</h3>
_Giulia_ _Silvestrini_
