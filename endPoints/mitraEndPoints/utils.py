from mitraEndPoints import settings

class common():
    
    def isBool(self, value):
        valid = {'true': True, 't': True, '1': True, 'True': True,
                 'false': False, 'f': False, '0': False, 'False': False,
                 }   
    
        if isinstance(value, bool):
            return True
    
        if not isinstance(value, basestring):
            return False
    
        lower_value = value.lower()
        if lower_value in valid:
            return True
        else:
            return False

    def getBoolValue(self, value):
        valid = {'true': True, 't': True, '1': True, 'True': True,
                 'false': False, 'f': False, '0': False, 'False': False,
                 }   
    
        if isinstance(value, bool):
            return value
        
        if not isinstance(value, basestring):
            return None
        
        lower_value = value.lower()
        if lower_value in valid:
            return valid[lower_value]
        else:
            return None
        
    def getBaseURL(self, dirName):
        basicURL  = settings.DOMAIN_NAME + settings.STATIC_URL + dirName 
        return basicURL