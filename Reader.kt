package lab2

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.w3c.dom.Document
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.io.FileInputStream
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.containsValue as containsValue1


class Reader {
    var hashMap: HashMap<Guide, Int> = hashMapOf() //карта, которая содержит адрес и количество повторений этого адреса
    private val cities = mutableListOf<String>()

    fun readFromConsole(){
        var exit = false
        var path: String

        while(!exit){
            println ("Enter the path to the file.")
            println ("If you want to finish the work, click R")
            path = readLine().toString()
            if (path == "R" || path == "К") exit = true
            else {
                if (path.lowercase().endsWith(".csv"))
                    readCSV(path)
                else if (path.lowercase().endsWith(".xml"))
                    readXML(path)
                else throw Exception("Path entered incorrectly")
            }
        }
    }

    private fun readCSV(path: String){
        val startTime = System.currentTimeMillis()

        val inputStream = FileInputStream(path)
        val reader = inputStream.bufferedReader()
        val csvParser = CSVParser(reader, CSVFormat.DEFAULT
            .withDelimiter(';')
            .withQuote('"')
            .withRecordSeparator("\r\n")
            .withFirstRecordAsHeader()
            .withIgnoreHeaderCase()
            .withTrim()
        )

        for (csvRecord in csvParser) {
            val newGuide = Guide(
                csvRecord.get("city"),
                csvRecord.get("street"),
                csvRecord.get("house").toInt(),
                csvRecord.get("floor").toInt()
            )
            if (hashMap.isEmpty() || !hashMap.containsKey(newGuide)) //если не повторялась запись, то 1
                hashMap[newGuide] = 1
            else {
                hashMap[newGuide] = hashMap[newGuide]!! + 1 //если не NULL, то добавляем единичку
            }

            if (!cities.contains(newGuide.city)) cities.add(newGuide.city)

        }

        val runTime = System.currentTimeMillis() - startTime
        println("\nRuntime is $runTime milliseconds\n")

        printStatistic()
    }

    private fun readXML(path:String){
        val startTime = System.currentTimeMillis()

        val builderFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        val docBuilder: DocumentBuilder = builderFactory.newDocumentBuilder()
        val doc: Document = docBuilder.parse(File(path))
        val elements: NodeList = doc.documentElement.getElementsByTagName("item")

        for (index in 0 until elements.length){
            val element = elements.item(index)
            val attributes = element.attributes
            val newGuide = Guide(
                attributes.getNamedItem("city").nodeValue,
                attributes.getNamedItem("street").nodeValue,
                attributes.getNamedItem("house").nodeValue.toInt(),
                attributes.getNamedItem("floor").nodeValue.toInt()
            )

            if (hashMap.isEmpty() || !hashMap.containsKey(newGuide)) //если не повторялась запись, то 1
                hashMap[newGuide] = 1
            else {
                hashMap[newGuide] = hashMap[newGuide]!! + 1 //если не NULL, то добавляем единичку
            }

            if (!cities.contains(newGuide.city)) cities.add(newGuide.city)

        }

        val runTime = System.currentTimeMillis() - startTime
        println("\nRuntime is $runTime milliseconds\n")

        printStatistic()
    }

    private fun printStatistic (){
        //дубликаты
        hashMap.forEach{
            if (it.value > 1)
                println("${it.key} : number of repetitions - ${it.value}")
        }
        println()

        //вывод 1..5 этажных домов каждого города
        cities.sort()
        var cntHouses: Int
        for (city in cities) {
            val count = hashMap.filterKeys { city == it.city }
            for (indexFloor in 1..5) {
                cntHouses = count.filterKeys { indexFloor == it.floor }.count()
                if (cntHouses!=0)
                    println("$city has $cntHouses $indexFloor-storey houses")
            }
        }

        clear()
    }

    private fun clear(){
        hashMap.clear()
        cities.clear()
    }
}


