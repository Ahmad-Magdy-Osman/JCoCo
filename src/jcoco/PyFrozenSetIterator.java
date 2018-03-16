/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcoco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author xmasterrrr
 */
public class PyFrozenSetIterator extends PyPrimitiveTypeAdapter {
    private PyFrozenSet frozenSet;
    private Iterator<PyObject> hashSetIterator;
    
    public PyFrozenSetIterator(PyFrozenSet frozenSet) {
        super("frozen_set_iterator", PyType.PyTypeId.PyFrozenSetIteratorType);
        this.frozenSet = frozenSet;
        initMethods(funs());
        this.hashSetIterator=this.frozenSet.hashSet().iterator();
    }
    
    public static HashMap<String, PyCallable> funs() {
        HashMap<String, PyCallable> funs = new HashMap<String, PyCallable>();

        funs.put("__iter__", new PyCallableAdapter() {
            @Override
            public PyObject __call__(ArrayList<PyObject> args) {
                if (args.size() != 1)
                    throw new PyException(PyException.ExceptionType.PYWRONGARGCOUNTEXCEPTION, 
                            "TypeError: expected 1 arguments, got " + args.size() + ".");
                PyFrozenSetIterator self = (PyFrozenSetIterator) args.get(args.size() - 1);
                return self;
            }
        });

        funs.put("__next__", new PyCallableAdapter() {
            @Override
            public PyObject __call__(ArrayList<PyObject> args) {
                if (args.size() != 1)
                    throw new PyException(PyException.ExceptionType.PYWRONGARGCOUNTEXCEPTION, 
                            "TypeError: expected 1 arguments, got " + args.size() + ".");
                PyFrozenSetIterator self = (PyFrozenSetIterator) args.get(args.size() - 1);
                if (self.hashSetIterator.hasNext())
                    return self.hashSetIterator.next();
                else
                    throw new PyException(PyException.ExceptionType.PYSTOPITERATIONEXCEPTION, "stop it");
            }
        });

        return funs;
    }
}