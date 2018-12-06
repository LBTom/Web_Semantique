import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;
import org.apache.log4j.BasicConfigurator;


public class main {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        Model model = FileManager.get().loadModel("landslide.ttl");
        Model model2 = FileManager.get().loadModel("earthquake.ttl");
        Model unifiedmodel = model.union(model2);
       
        
        /********************************************************************************************/
        /* 																							*/
        /* 										Requête 1											*/
        /* 																							*/
        /* 				Les dix pays ayant subis le plus de glissements de terrain					*/	
        /* 																							*/
        /********************************************************************************************/
        
        String queryString1 =
                "PREFIX schema: <http://schema.org/> "+
                "PREFIX moac: <http://www.observedchange.com/moac/ns#> "+
                "PREFIX dbo: <http://dbpedia.org/ontology/> "+
                "PREFIX dbpa: <http://dbpedia.org/page/> "+
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "+
                "PREFIX rdf: <https://www.w3.org/TR/rdf-schema#> "+
                "SELECT ?country (COUNT(?landslide) AS ?countlandslide) WHERE {?landslide a moac:Landslides . ?landslide schema:Country ?country} GROUP BY ?country ORDER BY DESC(?countlandslide) LIMIT 10";
           
           
           
        /********************************************************************************************/
        /* 																							*/
        /* 										Requête 2											*/
        /* 																							*/
        /* 					Les 10 glissements de terrains les plus meurtriers						*/	
        /* 																							*/
        /********************************************************************************************/
        
       String queryString2 =
                "PREFIX geo: <http://www.w3.org/2003/01/geo#>"+
                "PREFIX frapo: <http://purl.org/cerif/frapo/>"+
                "PREFIX nh: <http://www.w3.org/ns#>"+
                "PREFIX dbpac: <http://dbpedia.org/page/Category:>"+
                "PREFIX sosa: <http://www.w3.org/ns/sosa#>"+
                "PREFIX schema: <http://schema.org/> "+
                "PREFIX moac: <http://www.observedchange.com/moac/ns#> "+
                "PREFIX dbo: <http://dbpedia.org/ontology#> "+
                "PREFIX dbpa: <http://dbpedia.org/page/> "+
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "+
                "PREFIX rdf: <https://www.w3.org/TR/rdf-schema#> "+
                "SELECT ?country (COUNT(DISTINCT ?landslide) AS ?countlandslide) (COUNT(DISTINCT ?earthquake) AS ?countearthquake) WHERE {"+
                "?landslide a moac:Landslides . ?landslide schema:Country ?country1 ."+
                "BIND(UCASE(?country1) AS ?country) ."+
                "?earthquake a dbo:Earthquake . ?earthquake dbo:locationCountry ?country}"+
                "GROUP BY ?country ORDER BY ASC(?country) LIMIT 10";
	
       
       /********************************************************************************************/
       /* 																							*/
       /* 										Requête 3											*/
       /* 																							*/
       /* 					Nombre de glissments de terrien et de seismes pour 10 pays				*/	
       /* 																							*/
       /********************************************************************************************/
              
	 
       String queryStringUnion =
                "PREFIX geo: <http://www.w3.org/2003/01/geo#>"+
                "PREFIX frapo: <http://purl.org/cerif/frapo/>"+
                "PREFIX nh: <http://www.w3.org/ns#>"+
                "PREFIX dbpac: <http://dbpedia.org/page/Category:>"+
                "PREFIX sosa: <http://www.w3.org/ns/sosa#>"+
                "PREFIX schema: <http://schema.org/> "+
                "PREFIX moac: <http://www.observedchange.com/moac/ns#> "+
                "PREFIX dbo: <http://dbpedia.org/ontology#> "+
                "PREFIX dbpa: <http://dbpedia.org/page/> "+
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "+
                "PREFIX rdf: <https://www.w3.org/TR/rdf-schema#> "+
                "SELECT ?country (COUNT(DISTINCT ?lanslide) AS ?countlandslide) (COUNT(DISTINCT ?earthquake) AS ?countearthquake)\r\n" + 
                "WHERE { \r\n" + 
                "?landslide a moac:Landslides . \r\n" + 
                "?landslide schema:Country ?country1 . BIND(UCASE(?country1) AS ?country) . ?earthquake a dbo:Earthquake . \r\n" + 
                "?earthquake dbo:locationCountry ?country }\r\n" + 
                "GROUP BY ?country \r\n" + 
                "ORDER BY ASC(?country) \r\n" + 
                "LIMIT 10";
       
       
       
       
       Query query1 = QueryFactory.create(queryString1);
       QueryExecution qexec1 = QueryExecutionFactory.create(query1, unifiedmodel);

       ResultSet results1 = qexec1.execSelect();
       ResultSetFormatter.out(System.out,results1,query1);
       qexec1.close();
       
       
       
       Query query2 = QueryFactory.create(queryString2);
       QueryExecution qexec2 = QueryExecutionFactory.create(query2, unifiedmodel);

       ResultSet results2 = qexec2.execSelect();
       ResultSetFormatter.out(System.out,results2,query2);
       qexec2.close();
       


        Query query3 = QueryFactory.create(queryStringUnion);
        QueryExecution qexec3 = QueryExecutionFactory.create(query3, unifiedmodel);

        ResultSet results3 = qexec3.execSelect();
        ResultSetFormatter.out(System.out,results3,query3);
        qexec3.close();
        
       /* try {
            int size = 0;
            ResultSet results = qexec.execSelect();
            while ( results.hasNext() ) {
                QuerySolution soln = results.nextSolution();
                Resource res = soln.getResource()
                Literal casualties = soln.getLiteral("casualties");
                System.out.println("Resource: "+res.toString()+" Casualties: "+casualties);
                size++;
            }
            System.out.println("Results size: "+size);
        } finally {
            qexec.close();
        }*/
    }
}
