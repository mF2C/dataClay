package consumer;

import es.bsc.compss.types.annotations.task.Method;
import es.bsc.compss.types.annotations.Parameter;
import es.bsc.compss.types.annotations.parameter.Direction;
import es.bsc.compss.types.annotations.parameter.Type;
import model.Text;
import model.TextStats;

public interface WordcountItf {
    @Method(declaringClass = "consumer.Wordcount")
    public TextStats wordCountNewStats(@Parameter(type = Type.OBJECT, direction = Direction.IN) Text text);

    @Method(declaringClass = "consumer.Wordcount")
    public TextStats reduceTaskIN(@Parameter(type = Type.OBJECT, direction = Direction.IN) TextStats m1,
	    @Parameter(type = Type.OBJECT, direction = Direction.IN) TextStats m2);
}
