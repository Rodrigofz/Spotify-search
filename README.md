# 2020-Spotify-rank-and-search

Ranking diseñado para canciones de spotify basado en posiciones del top 200, cantidad de reproducciones y distancia a un país de referencia. [Roberto Aguilera, Javiera D. Mizunuma, Rodrigo Fuentes. Grupo 2]

## Idea general

La idea del proyecto es descubrir cómo varían las canciones más populares a través del mundo, tomando en cuenta las posiciones en el [TOP 200 de los rankings de Spotify](https://www.kaggle.com/edumucelli/spotifys-worldwide-daily-song-ranking), la cantidad de reproducciones totales de cada canción en todos los paises donde es escuchada y las distancia de los paises donde estas son reproducidas a uno de referencia.

Analizando esta información esperamos averiguar si existen correlaciones entre el lugar geográfico y la música que más se escucha o si la globalización ha avanzado tanto que lo más popular se ha independizado de la cultura de cada país.

Para esto, se usará los [siguientes datos](./data.zip). Estos están pre procesados para limpiarlos. Los datos sin procesar están [aquí](./raw_data.zip)

Se pretenden seguir lo siguientes pasos:

- Diseñar y calcular un ranking de importancia de las canciones, basado tanto en las posiciones alcanzadas por la canción, como en la cantidad de reproducciones que esta tuvo y las distancias a un pais de referencia.

- Utilizar dicho ranking para crear un índice sobre las canciones del dataset.

- Crear un buscador mediante elastic-search que utilice el indice para entregar resultados acorde al pais de referencia, de tal manera que las canciones más importantes aparezcan primero.

## Data

## Métodos

## Resultados

### Top 10 reproducciones

Las mejores 10 canciones según el ranking, considerando solo las reproducciones.

| Artista | Canción | Puntaje
|---|---|---|
| Jeezy | "American Dream (feat)" | 7.81054960186701E-4
| blackbear | playboy shit (feat. lil aaron) | 6.908964833405943E-4
| Lin-Manuel Miranda | Almost Like Praying (feat. Artists for Puerto Rico) | 6.612622023153749E-4
| Tom Petty and the Heartbreakers | The Waiting | 6.463402417604166E-4
| Tom Petty and the Heartbreakers | Here Comes My Girl | 6.395081817370284E-4
| Gucci Mane | Mall | 6.207740817471856E-4
| G-Eazy | Nothing Wrong | 6.182771580015577E-4
| Bruce Springsteen | Born In The U.S.A. | 6.132634498706992E-4
| Ty Dolla $ign | Lil Favorite (feat. MadeinTYO) | 6.091125761936989E-4
| Gucci Mane | Peepin out the Blinds | 6.01764139540636E-4

### Top 10 posiciones en el top 200

Las mejores 10 canciones según el ranking, considerando solo las posiciones en las que estuvo la canción en algún top 200 en algún país.

| Artista | Canción | Puntaje
|---|---|---|
| KESI | Ligesom Mig | 7.151963511065273E-5
| Pikku G | Solmussa (feat. BEHM) | 7.148351408281907E-5
| Floni | Ungir Strákar - Deep Mix | 7.127528698118972E-5
| Birnir| Já ég veit | 7.120142605592761E-5
| Ezhel | İmkansızım | 7.102464324774698E-5
| Shade | Irraggiungibile (feat. Federica) | 7.095106337623397E-5
| Chase | Ég Vil Það | 7.082862414340004E-5
| Birnir | Út í geim | 7.069996563288884E-5
| Ersay Üner | İki Aşık | 7.052054285142376E-5
| Nitro | Infamity Show | 7.043600427564284E-5

### Top 10 distancia desde Chile

10 de las mejores canciones según el ranking, considerando solo la distancia hacia Chile (Por eso todas tienen el mismo puntaje).

| Artista | Canción | Puntaje
|---|---|---|
| "Franco ""El Gorilla""" | Bailen (Remix) | 6.108237711118094E-4
| Michael Bublé | Christmas (Baby Please Come Home) | 6.108237711118094E-4
| Sam Smith | Stay With Me | 6.108237711118094E-4
| Sonora Malecón | Melosa | 6.108237711118094E-4
| Maroon 5 | Don't Wanna Know (feat. Kendrick Lamar) | 6.108237711118094E-4
| Demi Lovato | Sorry Not Sorry | 6.108237711118094E-4
| Sia | Cheap Thrills | 6.108237711118094E-4
| Luis Miguel | Llego La Navidad (Winter Wonderland) | 6.108237711118094E-4
| Alessia Cara | "How Far I'll Go - From ""Moana""" | 6.108237711118094E-4
| Soundgarden | Rusty Cage | 6.108237711118094E-4

### Top 10 Chile

Las mejores 10 canciones según el ranking, considerando las reproducciones, posiciones y además la distancia a Chile.

| Artista | Canción | Puntaje
|---|---|---|
| Anitta | Vai malandra (feat. Tropkillaz e DJ Yuri Martins) | 5.130947105517517E-4
| Post Malone | rockstar | 4.811787550831518E-4
| Major Lazer | Sua Cara (feat. Anitta & Pabllo Vittar) | 4.6946842840846036E-4
| Ed Sheeran | Shape of You | 4.6914858217336595E-4
| Soundgarden | Burden In My Hand | 4.687756220883004E-4
| Luis Fonsi | Despacito - Remix | 4.670383870550586E-4
| Drake | Free Smoke | 4.649374689773587E-4
| Eminem | River (feat. Ed Sheeran) | 4.64760046748282E-4
| Camila Cabello | Havana | 4.63292588251183E-4
| Sam Smith | Too Good At Goodbyes - Edit | 4.632317596483783E-4

### Top 10 Estados Unidos

Las mejores 10 canciones según el ranking, considerando las reproducciones, posiciones y además la distancia a Estados Unidos.

| Artista | Canción | Puntaje
|---|---|---|
| Jeezy | "American Dream (feat)" | 4.4669041787373076E-4
| blackbear | playboy shit (feat. lil aaron) | 4.286587225045094E-4
| Lin-Manuel Miranda | Almost Like Praying (feat. Artists for Puerto Rico) | 4.220455667706259E-4
| Tom Petty and the Heartbreakers | The Waiting | 4.190972956874679E-4
| Tom Petty and the Heartbreakers | Here Comes My Girl | 4.1758639957145563E-4
| G-Eazy | Nothing Wrong | 4.142793415480367E-4
| Bruce Springsteen | Born In The U.S.A. | 4.134210840331997E-4
| Gucci Mane | Mall | 4.12882372335895E-4
| Ty Dolla $ign | Lil Favorite (feat. MadeinTYO) | 4.1157952051845703E-4
| Gucci Mane | Peepin out the Blinds | 4.0879141567191576E-4

### Top 10 Alemania

Las mejores 10 canciones según el ranking, considerando las reproducciones, posiciones y además la distancia a Alemania.

| Artista | Canción | Puntaje
|---|---|---|
| Kodak Black | Roll In Peace (feat. XXXTENTACION) | 1.7034891512072943E-4
| Bausa | Was Du Liebe nennst | 1.681157227647236E-4
| Jeezy | "American Dream (feat)" | 1.618733774544751E-4
| Lil Uzi Vert | Two® | 1.6124564313384928E-4
| Lil Uzi Vert | Sauce It Up | 1.6100463497160415E-4
| Lil Uzi Vert | 444+222 | 1.6070118322090087E-4
| Future | Rent Money | 1.5871443221579512E-4
| Big Sean | Go Legend (& Metro Boomin) | 1.519541938807858E-4
| Big Sean | Jump Out The Window | 1.5107802220292295E-4
| 6ix9ine | GUMMO | 1.495800024531379E-4

### Top 10 Honk Kong

Las mejores 10 canciones según el ranking, considerando las reproducciones, posiciones y además la distancia a Honk Kong.

| Artista | Canción | Puntaje
|---|---|---|
| Chance The Rapper | Juke Jam (feat. Justin Bieber & Towkio) | 2.805433439128051E-4
| Thomas Rhett | Craving You | 2.712499973652033E-4
| Thomas Rhett | Unforgettable | 2.7101247128423055E-4
| Post Malone | rockstar | 2.611522114399616E-4
| Kodak Black | Codeine Dreaming (feat. Lil Wayne) | 2.5626585629821906E-4
| Lil Uzi Vert | The Way Life Goes (feat. Nicki Minaj & Oh Wonder) - Remix | 2.5515132538361584E-4
| Miguel | Sky Walker | 2.524229079884676E-4
| Drake | Gyalchester | 2.5167145357289785E-4
| Drake | Sacrifices | 2.5097445897865884E-4
| Kendrick Lamar | LOVE. FEAT. ZACARI. | 2.5025404920979194E-4

## Conclusiones
