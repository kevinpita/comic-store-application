<?xml version='1.0' encoding='ISO-8859-1' ?>
<!DOCTYPE helpset
  PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 1.0//EN"
         "http://java.sun.com/products/javahelp/helpset_1_0.dtd">
<helpset version="1.0">
   <!-- title --> 
   <title>Ayuda gestión de películas</title>
			
   <maps>
     <homeID>manual</homeID>
     <mapref location="mapa.jhm" />
   </maps>

   <view>
      <name>Tabla Contenidos</name>
      <label>Tabla de contenidos</label>
      <type>javax.help.TOCView</type>
      <data>tablacontenidos.xml</data>
   </view>
	
   <view>
      <name>Índice</name>
      <label>El Índice</label>
      <type>javax.help.IndexView</type>
      <data>indice.xml</data>
   </view>
	
   <view>
      <name>Buscar</name>
      <label>Buscar</label>
      <type>javax.help.SearchView</type>
         <data engine="com.sun.java.help.search.DefaultSearchEngine">
         JavaHelpSearch
         </data>
   </view>

   <view>
      <name>Favoritos</name>
      <label>Favoritos</label>
      <type>javax.help.FavoritesView</type>
   </view>

    <presentation default="true" displayviews="false" displayviewimages="true">
       <name>Ayuda</name>
       <size width="850" height="850" />
       <location x="300" y="200" />
       <title>Ayuda gestión de películas</title>
       <toolbar>
           <helpaction image="BackwardIco">javax.help.BackAction</helpaction>
           <helpaction image="ForwardIco">javax.help.ForwardAction</helpaction>
           <helpaction image="imgAnhadirFavorito">javax.help.FavoritesAction</helpaction>
       </toolbar>
   </presentation>
</helpset>