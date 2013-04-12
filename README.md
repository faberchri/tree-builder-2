Term Project

University of Zurich

Department of Informatics

Recommender System based on Hierarchical 2-Way Agglomerative Clustering
======

The implementation of a recommender system based on a novel clustering algorithmus. With a single training data set containing instances of the type content-user-rating the clustering algorithm creates one cluster hierarchy for the users and one for the content items. While the two hierarchchies are build from bottom up, clustering information is transferred between the hierarchies (2-way clustering). We show that with this exchange of information the recommender system generates more accurate rating predictions (MAE: 1.79, on a rating scale of 0 to 10) than without (MAE: 1.98). 



Responsibilities:

* alessandramacri
	* Testcases
* danihegglin
	* ch.uzh.agglorecommender.client
	* ch.uzh.agglorecommender.recommender (and subpackages)
	* ch.uzh.agglorecommender.util
	* ch.uzh.agglorecommender.visu
* faberchri
	* ch.uzh.agglorecommender.client
	* ch.uzh.agglorecommender.clusterer (and subpackages)
	* ch.uzh.agglorecommender.util
	* ch.uzh.agglorecommender.visu


