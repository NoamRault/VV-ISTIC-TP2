# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

### Quand est-ce que les métriques TCC et LCC produisent-elles la même valeur ?

Les métriques de *Tight Class Cohesion* (TCC) et de *Loose Class Cohesion* (LCC) produisent la même valeur pour une classe lorsque toutes les paires de méthodes de la classe sont directement connectées via des attributs partagés. Cela signifie que chaque méthode de la classe accède soit aux mêmes attributs, soit est indirectement connectée à d'autres méthodes qui partagent des attributs.

#### Exemple de classe

```java
public class Person {
    private String name;
    private int age;

    public void setNameAndAge(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getPersonInfo() {
        return name + " is " + age + " years old.";
    }

    public boolean getAge() {
        return age;
    }
}
```

#### Explication
- **TCC (Tight Class Cohesion)** : Mesure la proportion des paires de méthodes directement connectées. Dans cette classe, toutes les méthodes (*setNameAndAge*, *getPersonInfo*, *getAge*) interagissent avec l'attribut *age*. Ainsi, chaque paire de méthodes est directement connectée.
- **LCC (Loose Class Cohesion)** : Mesure la proportion des paires de méthodes qui sont directement ou indirectement connectées. Puisque toutes les paires de méthodes sont déjà directement connectées (comme calculé par TCC), les valeurs de TCC et de LCC seront identiques pour cette classe.

#### LCC peut-elle être inférieure à TCC ?
Non, LCC ne peut jamais être inférieure à TCC pour une classe. Cela s'explique par les raisons suivantes :

1. **TCC** ne prend en compte que les connexions directes entre les méthodes via des attributs.
2. **LCC** inclut à la fois les connexions directes et indirectes. Les connexions indirectes augmente le nombre de paires de méthodes connectées, donc LCC est toujours supérieure ou égale à TCC.

