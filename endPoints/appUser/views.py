from django.http import HttpResponse
from appUser.models import user

# Create your views here.

def index(request):
    queryset = user.objects.all()
    print queryset
    return HttpResponse("Hello, world. You're at the user's index.")