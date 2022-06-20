<?xml version='1.0' encoding='UTF-8' ?>
<!DOCTYPE helpset
  PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 1.0//EN"
         "http://java.sun.com/products/javahelp/helpset_1_0.dtd">
<helpset version="1.0">
   <!-- title --> 
   <title>Axuda xestión de bandas deseñadas</title>
			
   <maps>
     <homeID>manual</homeID>
     <mapref location="mapa.jhm" />
   </maps>

   <view>
      <name>Táboa de contidos</name>
      <label>Táboa de contidos</label>
      <type>javax.help.TOCView</type>
      <data>tablacontenidos.xml</data>
   </view>
	
   <view>
      <name>Índice</name>
      <label>O índice</label>
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
       <name>Axuda</name>
       <size width="850" height="850" />
       <location x="300" y="200" />
       <title>Axuda xestión de bandas deseñadas</title>
       <toolbar>
           <helpaction image="BackwardIco">javax.help.BackAction</helpaction>
           <helpaction image="ForwardIco">javax.help.ForwardAction</helpaction>
           <helpaction image="imgAnhadirFavorito">javax.help.FavoritesAction</helpaction>
       </toolbar>
   </presentation>
</helpset>